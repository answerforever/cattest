package feign;

import com.answer.test.request.MyCatContext;
import com.answer.test.request.MyCatType;
import com.answer.test.request.RequestHeaderContext;
import com.answer.test.request.Service3RdContext;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static feign.Util.checkArgument;
import static feign.Util.checkNotNull;
import com.answer.test.request.Response;

public class MyReflectiveFeign extends Feign {

    private final ParseHandlersByName targetToHandlersByName;
    private final InvocationHandlerFactory factory;

    MyReflectiveFeign(ParseHandlersByName targetToHandlersByName, InvocationHandlerFactory factory) {
        this.targetToHandlersByName = targetToHandlersByName;
        this.factory = factory;
    }

    /**
     * creates an api binding to the {@code target}. As this invokes reflection, care should be taken
     * to cache the result.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T newInstance(Target<T> target) {
        Map<String, InvocationHandlerFactory.MethodHandler> nameToHandler = targetToHandlersByName.apply(target);
        Map<Method, InvocationHandlerFactory.MethodHandler> methodToHandler = new LinkedHashMap<Method, InvocationHandlerFactory.MethodHandler>();
        List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();

        for (Method method : target.type().getMethods()) {
            if (method.getDeclaringClass() == Object.class) {
                continue;
            } else if(Util.isDefault(method)) {
                DefaultMethodHandler handler = new DefaultMethodHandler(method);
                defaultMethodHandlers.add(handler);
                methodToHandler.put(method, handler);
            } else {
                methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
            }
        }
        InvocationHandler handler = factory.create(target, methodToHandler);
        T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class<?>[] {target.type()}, handler);

        for(DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
            defaultMethodHandler.bindTo(proxy);
        }
        return proxy;
    }


    static class FeignInvocationHandler implements InvocationHandler {

        private final Target target;
        private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

        FeignInvocationHandler(Target target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
            this.target = checkNotNull(target, "target");
            this.dispatch = checkNotNull(dispatch, "dispatch for %s", target);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("equals".equals(method.getName())) {
                try {
                    Object
                            otherHandler =
                            args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                    return equals(otherHandler);
                } catch (IllegalArgumentException e) {
                    return false;
                }
            } else if ("hashCode".equals(method.getName())) {
                return hashCode();
            } else if ("toString".equals(method.getName())) {
                return toString();
            }

            Object response = null;
            Throwable exp = null;
            //RpcException rpcException = null;
            long start = System.nanoTime();
            String url = "";
            Map<String, String> service3RdContext = Service3RdContext.getContext();
            if (service3RdContext != null) {
                url = service3RdContext.get(Service3RdContext.URL);
            }
            String traceId = "";
            com.answer.test.request.Request.Header header = RequestHeaderContext.getHeaderContext();
            header = header == null ? new com.answer.test.request.Request.Header() : header;
            traceId = header.getxTraceId() == null ? "" : header.getxTraceId();

            //cat Transaction
            Transaction t = Cat.newTransaction(MyCatType.T_SERVICE_3RD.getCode(), url);

            try {
                //cat
                MyCatContext yqnCatContext = header.getMyCatContext() == null ? new MyCatContext() : header.getMyCatContext();
                Cat.logRemoteCallClient(yqnCatContext);
                //traceId赋值
                traceId = yqnCatContext.getProperty(Cat.Context.ROOT);
                //invoke
                response = dispatch.get(method).invoke(args);

            } catch (Throwable throwable) {
                t.setStatus(throwable);
                exp = throwable;
                //throw new ApplicationException("系统异常(" + traceId + "), 请联系管理员");
            } finally {
                service3RdContext = Service3RdContext.getContext();
                if (service3RdContext != null) {
                    url = service3RdContext.get(Service3RdContext.URL);
                }
                Service3RdContext.clear();

                Cat.logEvent(MyCatType.SERVICE_URL.getCode(), url);

                long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                String result = exp == null ? "success" : "fail";
//                Loggers.Service3RD.info(url, "consumer", String.valueOf(elapsedTime)
//                        , result, args, response, exp);

                String msg = this.getExceptionMsg(url, method.getDeclaringClass().getName(), method.getName(), (Response)response);

                if (exp == null && response == null) {
                    //rpcException = new RpcException(msg, url, method.getDeclaringClass().getName(), method.getName(), (Response)response);
                }

//                if (response instanceof com.yqn.model.Response
//                        && ((Response)response).getCode() <= Response.getErrorCode()
//                        && ((Response)response).getCode() != Result.getSuccessCode()) {
//                    if (CodeEnum.APPLICATION_ERROR.getCode().equals(((Response)response).getMsgCode())) {
//                        msg = ((Response)response).getMsg();
//                        rpcException = new RpcException(msg);
//                    } else {
//                        rpcException = new RpcException(msg, url, method.getDeclaringClass().getName(), method.getName(), (Response)response);
//                    }
//                }

                //cat
//                if (rpcException != null) {
//                    Cat.logEvent(YqnCatType.SERVICE_MSG.getCode(), msg);
//                    t.setStatus(rpcException);
//                } else {
//                    t.setStatus(Transaction.SUCCESS);
//                }
                t.setStatus(Transaction.SUCCESS);
                t.complete();
            }

//            if (rpcException != null) {
//                throw rpcException;
//            }

            return response;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FeignInvocationHandler) {
                FeignInvocationHandler other = (FeignInvocationHandler) obj;
                return target.equals(other.target);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return target.hashCode();
        }

        @Override
        public String toString() {
            return target.toString();
        }

        public String getExceptionMsg(String url, String interfaceName, String methodName, Response response) {
            String msg = "调用第三方服务失败。"
                    + "url: " + url
                    + ", interfaceName: " + interfaceName
                    + ", methodName: " + methodName
                    + ", response : " + (response == null ? "null" : response.toString());

            return msg;
        }
    }

    static final class ParseHandlersByName {

        private final Contract contract;
        private final Request.Options options;
        private final Encoder encoder;
        private final Decoder decoder;
        private final ErrorDecoder errorDecoder;
        private final SynchronousMethodHandler.Factory factory;

        ParseHandlersByName(Contract contract, Request.Options options, Encoder encoder, Decoder decoder,
                            ErrorDecoder errorDecoder, SynchronousMethodHandler.Factory factory) {
            this.contract = contract;
            this.options = options;
            this.factory = factory;
            this.errorDecoder = errorDecoder;
            this.encoder = checkNotNull(encoder, "encoder");
            this.decoder = checkNotNull(decoder, "decoder");
        }

        public Map<String, InvocationHandlerFactory.MethodHandler> apply(Target key) {
            List<MethodMetadata> metadata = contract.parseAndValidatateMetadata(key.type());
            Map<String, InvocationHandlerFactory.MethodHandler> result = new LinkedHashMap<String, InvocationHandlerFactory.MethodHandler>();
            for (MethodMetadata md : metadata) {
                BuildTemplateByResolvingArgs buildTemplate;
                if (!md.formParams().isEmpty() && md.template().bodyTemplate() == null) {
                    buildTemplate = new BuildFormEncodedTemplateFromArgs(md, encoder);
                } else if (md.bodyIndex() != null) {
                    buildTemplate = new BuildEncodedTemplateFromArgs(md, encoder);
                } else {
                    buildTemplate = new BuildTemplateByResolvingArgs(md);
                }
                result.put(md.configKey(),
                        factory.create(key, md, buildTemplate, options, decoder, errorDecoder));
            }
            return result;
        }
    }

    private static class BuildTemplateByResolvingArgs implements RequestTemplate.Factory {

        protected final MethodMetadata metadata;
        private final Map<Integer, Param.Expander> indexToExpander = new LinkedHashMap<Integer, Param.Expander>();

        private BuildTemplateByResolvingArgs(MethodMetadata metadata) {
            this.metadata = metadata;
            if (metadata.indexToExpander() != null) {
                indexToExpander.putAll(metadata.indexToExpander());
                return;
            }
            if (metadata.indexToExpanderClass().isEmpty()) {
                return;
            }
            for (Map.Entry<Integer, Class<? extends Param.Expander>> indexToExpanderClass : metadata
                    .indexToExpanderClass().entrySet()) {
                try {
                    indexToExpander
                            .put(indexToExpanderClass.getKey(), indexToExpanderClass.getValue().newInstance());
                } catch (InstantiationException e) {
                    throw new IllegalStateException(e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        @Override
        public RequestTemplate create(Object[] argv) {
            RequestTemplate mutable = new RequestTemplate(metadata.template());
            if (metadata.urlIndex() != null) {
                int urlIndex = metadata.urlIndex();
                checkArgument(argv[urlIndex] != null, "URI parameter %s was null", urlIndex);
                mutable.insert(0, String.valueOf(argv[urlIndex]));
            }
            Map<String, Object> varBuilder = new LinkedHashMap<String, Object>();
            for (Map.Entry<Integer, Collection<String>> entry : metadata.indexToName().entrySet()) {
                int i = entry.getKey();
                Object value = argv[entry.getKey()];
                if (value != null) { // Null values are skipped.
                    if (indexToExpander.containsKey(i)) {
                        value = expandElements(indexToExpander.get(i), value);
                    }
                    for (String name : entry.getValue()) {
                        varBuilder.put(name, value);
                    }
                }
            }

            RequestTemplate template = resolve(argv, mutable, varBuilder);
            if (metadata.queryMapIndex() != null) {
                // add query map parameters after initial resolve so that they take
                // precedence over any predefined values
                template = addQueryMapQueryParameters((Map<String, Object>) argv[metadata.queryMapIndex()], template);
            }

            if (metadata.headerMapIndex() != null) {
                template = addHeaderMapHeaders((Map<String, Object>) argv[metadata.headerMapIndex()], template);
            }

            return template;
        }

        private Object expandElements(Param.Expander expander, Object value) {
            if (value instanceof Iterable) {
                return expandIterable(expander, (Iterable) value);
            }
            return expander.expand(value);
        }

        private List<String> expandIterable(Param.Expander expander, Iterable value) {
            List<String> values = new ArrayList<String>();
            for (Object element : (Iterable) value) {
                if (element!=null) {
                    values.add(expander.expand(element));
                }
            }
            return values;
        }

        @SuppressWarnings("unchecked")
        private RequestTemplate addHeaderMapHeaders(Map<String, Object> headerMap, RequestTemplate mutable) {
            for (Map.Entry<String, Object> currEntry : headerMap.entrySet()) {
                Collection<String> values = new ArrayList<String>();

                Object currValue = currEntry.getValue();
                if (currValue instanceof Iterable<?>) {
                    Iterator<?> iter = ((Iterable<?>) currValue).iterator();
                    while (iter.hasNext()) {
                        Object nextObject = iter.next();
                        values.add(nextObject == null ? null : nextObject.toString());
                    }
                } else {
                    values.add(currValue == null ? null : currValue.toString());
                }

                mutable.header(currEntry.getKey(), values);
            }
            return mutable;
        }

        @SuppressWarnings("unchecked")
        private RequestTemplate addQueryMapQueryParameters(Map<String, Object> queryMap, RequestTemplate mutable) {
            for (Map.Entry<String, Object> currEntry : queryMap.entrySet()) {
                Collection<String> values = new ArrayList<String>();

                boolean encoded = metadata.queryMapEncoded();
                Object currValue = currEntry.getValue();
                if (currValue instanceof Iterable<?>) {
                    Iterator<?> iter = ((Iterable<?>) currValue).iterator();
                    while (iter.hasNext()) {
                        Object nextObject = iter.next();
                        values.add(nextObject == null ? null : encoded ? nextObject.toString() : RequestTemplate.urlEncode(nextObject.toString()));
                    }
                } else {
                    values.add(currValue == null ? null : encoded ? currValue.toString() : RequestTemplate.urlEncode(currValue.toString()));
                }

                mutable.query(true, encoded ? currEntry.getKey() : RequestTemplate.urlEncode(currEntry.getKey()), values);
            }
            return mutable;
        }

        protected RequestTemplate resolve(Object[] argv, RequestTemplate mutable,
                                          Map<String, Object> variables) {
            // Resolving which variable names are already encoded using their indices
            Map<String, Boolean> variableToEncoded = new LinkedHashMap<String, Boolean>();
            for (Map.Entry<Integer, Boolean> entry : metadata.indexToEncoded().entrySet()) {
                Collection<String> names = metadata.indexToName().get(entry.getKey());
                for (String name : names) {
                    variableToEncoded.put(name, entry.getValue());
                }
            }
            return mutable.resolve(variables, variableToEncoded);
        }
    }

    private static class BuildFormEncodedTemplateFromArgs extends BuildTemplateByResolvingArgs {

        private final Encoder encoder;

        private BuildFormEncodedTemplateFromArgs(MethodMetadata metadata, Encoder encoder) {
            super(metadata);
            this.encoder = encoder;
        }

        @Override
        protected RequestTemplate resolve(Object[] argv, RequestTemplate mutable,
                                          Map<String, Object> variables) {
            Map<String, Object> formVariables = new LinkedHashMap<String, Object>();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                if (metadata.formParams().contains(entry.getKey())) {
                    formVariables.put(entry.getKey(), entry.getValue());
                }
            }
            try {
                encoder.encode(formVariables, Encoder.MAP_STRING_WILDCARD, mutable);
            } catch (EncodeException e) {
                throw e;
            } catch (RuntimeException e) {
                throw new EncodeException(e.getMessage(), e);
            }
            return super.resolve(argv, mutable, variables);
        }
    }

    private static class BuildEncodedTemplateFromArgs extends BuildTemplateByResolvingArgs {

        private final Encoder encoder;

        private BuildEncodedTemplateFromArgs(MethodMetadata metadata, Encoder encoder) {
            super(metadata);
            this.encoder = encoder;
        }

        @Override
        protected RequestTemplate resolve(Object[] argv, RequestTemplate mutable,
                                          Map<String, Object> variables) {
            Object body = argv[metadata.bodyIndex()];
            checkArgument(body != null, "Body parameter %s was null", metadata.bodyIndex());
            try {
                encoder.encode(body, metadata.bodyType(), mutable);
            } catch (EncodeException e) {
                throw e;
            } catch (RuntimeException e) {
                throw new EncodeException(e.getMessage(), e);
            }
            return super.resolve(argv, mutable, variables);
        }
    }
}
