package com.wbl.weather.network.errorhandler;


import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 网络错误处理
 * @param <T>
 */
public class HttpErrorHandler<T> implements Function<Throwable, Observable<T>> {
    /**
     * 处理以下两类网络错误
     * 1、http请求相关的错误，例如：404，403，socket，timeout等
     * 2、应用数据的错误会抛出RuntimeException，最后也会走到这个函数进行统一的处理
     */
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        //通过这个异常处理，得到用户可以知道的原因
        return Observable.error(ExceptionHandle.handleException(throwable));
    }
}
