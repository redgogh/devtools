package org.redgogh.devtools.lang.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author RedGogh
 * @since 1.0
 */
public class OpenFileDescriptorException extends SystemRuntimeException {

    /**
     * 默认构造函数。
     */
    public OpenFileDescriptorException() {
    }

    /**
     * 通过已有的异常创建一个新的断言异常。
     *
     * @param e 原始异常，不能为 null。
     */
    public OpenFileDescriptorException(@NotNull Throwable e) {
        super(e);
    }

    /**
     * 根据格式化字符串和参数创建异常信息。
     *
     * @param vfmt 格式化字符串，不能为 null。
     * @param args 格式化参数。
     */
    public OpenFileDescriptorException(@NotNull String vfmt, @NotNull Object... args) {
        super(vfmt, args);
    }

    /**
     * 根据格式化字符串、已有异常和参数创建异常信息。
     *
     * @param vfmt 格式化字符串，不能为 null。
     * @param e 原始异常，不能为 null。
     * @param args 格式化参数。
     */
    public OpenFileDescriptorException(@NotNull String vfmt, @NotNull Throwable e, @NotNull Object... args) {
        super(vfmt, e, args);
    }
}