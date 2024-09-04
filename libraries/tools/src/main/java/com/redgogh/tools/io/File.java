package com.redgogh.tools.io;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2023 RedGogh                                                    *|
|*                                                                                  *|
|*    This program is free software: you can redistribute it and/or modify          *|
|*    it under the terms of the GNU General Public License as published by          *|
|*    the Free Software Foundation, either version 3 of the License, or             *|
|*    (at your option) any later version.                                           *|
|*                                                                                  *|
|*    This program is distributed in the hope that it will be useful,               *|
|*    but WITHOUT ANY WARRANTY; without even the implied warranty of                *|
|*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *|
|*    GNU General Public License for more details.                                  *|
|*                                                                                  *|
|*    You should have received a copy of the GNU General Public License             *|
|*    along with this program.  If not, see <https://www.gnu.org/licenses/>.        *|
|*                                                                                  *|
|*    This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.    *|
|*    This is free software, and you are welcome to redistribute it                 *|
|*    under certain conditions; type `show c' for details.                          *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

import com.redgogh.tools.SystemOS;
import com.redgogh.tools.collection.Collects;

import java.io.*;
import java.net.URI;
import java.util.List;

import static com.redgogh.tools.Assert.throwIfError;
import static com.redgogh.tools.Assert.xassert;
import static com.redgogh.tools.Optional.optionalIfError;
import static com.redgogh.tools.StringUtils.*;

/**
 * File 对象，增强 {@code File} 对象操作，添加更多实用函数以便在开发中
 * 提高效率，代码简洁。
 */
public class File extends java.io.File {

    /**
     * 空的 <code>File</code> 数组对象
     */
    private static final java.io.File[] EMPTY_FILE_ARRAY = new java.io.File[0];

    private static final String PATHNAME_DESKTOP_VARIABLE = "Desktop://";

    /**
     * 文件随机读写访问对象
     */
    private RandomAccessFile accessFile;

    /**
     * #brief: 快速访问指定路径
     *
     * 该函数用于在 Windows 操作系统上对指定路径进行快速替换操作。
     * 如果传入的路径以特定的桌面路径变量（{@code PATHNAME_DESKTOP_VARIABLE}）开头，
     * 将替换为实际的用户桌面路径。
     *
     * 注意事项：
     * - 该函数仅在 Windows 操作系统上执行替换操作，其他操作系统则直接返回原始路径。
     * - 确保 {@code PATHNAME_DESKTOP_VARIABLE} 已正确定义且匹配预期的路径格式。
     *
     * @param pathname
     *        要进行快速访问转换的路径字符串
     *
     * @return 如果是 Windows 操作系统且路径匹配桌面路径变量，返回替换后的路径；
     *         否则，返回原始路径
     */
    private static String quickAccessPath(String pathname) {
        if (SystemOS.isWindows()) {
            /* Conversation Desktop */
            if (pathname.startsWith(PATHNAME_DESKTOP_VARIABLE))
                return pathname.replace(PATHNAME_DESKTOP_VARIABLE, SystemOS.getUserHome("Desktop/"));
        }

        return pathname;
    }

    /**
     * Creates a new <code>File</code> instance by converting the given
     * pathname string into an abstract pathname.  If the given string is
     * the empty string, then the result is the empty abstract pathname.
     *
     * @param   pathname  A pathname string
     * @throws  NullPointerException
     *          If the <code>pathname</code> argument is <code>null</code>
     */
    public File(String pathname) {
        super(quickAccessPath(pathname));
    }

    /**
     * Creates a new <code>File</code> instance from a parent pathname string
     * and a child pathname string.
     *
     * <p> If <code>parent</code> is <code>null</code> then the new
     * <code>File</code> instance is created as if by invoking the
     * single-argument <code>File</code> constructor on the given
     * <code>child</code> pathname string.
     *
     * <p> Otherwise the <code>parent</code> pathname string is taken to denote
     * a directory, and the <code>child</code> pathname string is taken to
     * denote either a directory or a file.  If the <code>child</code> pathname
     * string is absolute then it is converted into a relative pathname in a
     * system-dependent way.  If <code>parent</code> is the empty string then
     * the new <code>File</code> instance is created by converting
     * <code>child</code> into an abstract pathname and resolving the result
     * against a system-dependent default directory.  Otherwise each pathname
     * string is converted into an abstract pathname and the child abstract
     * pathname is resolved against the parent.
     *
     * @param   parent  The parent pathname string
     * @param   child   The child pathname string
     * @throws  NullPointerException
     *          If <code>child</code> is <code>null</code>
     */
    public File(String parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new <code>File</code> instance from a parent abstract
     * pathname and a child pathname string.
     *
     * <p> If <code>parent</code> is <code>null</code> then the new
     * <code>File</code> instance is created as if by invoking the
     * single-argument <code>File</code> constructor on the given
     * <code>child</code> pathname string.
     *
     * <p> Otherwise the <code>parent</code> abstract pathname is taken to
     * denote a directory, and the <code>child</code> pathname string is taken
     * to denote either a directory or a file.  If the <code>child</code>
     * pathname string is absolute then it is converted into a relative
     * pathname in a system-dependent way.  If <code>parent</code> is the empty
     * abstract pathname then the new <code>File</code> instance is created by
     * converting <code>child</code> into an abstract pathname and resolving
     * the result against a system-dependent default directory.  Otherwise each
     * pathname string is converted into an abstract pathname and the child
     * abstract pathname is resolved against the parent.
     *
     * @param   parent  The parent abstract pathname
     * @param   child   The child pathname string
     * @throws  NullPointerException
     *          If <code>child</code> is <code>null</code>
     */
    public File(java.io.File parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new <tt>File</tt> instance by converting the given
     * <tt>file:</tt> URI into an abstract pathname.
     *
     * <p> The exact form of a <tt>file:</tt> URI is system-dependent, hence
     * the transformation performed by this constructor is also
     * system-dependent.
     *
     * <p> For a given abstract pathname <i>f</i> it is guaranteed that
     *
     * <blockquote><tt>
     * new File(</tt><i>&nbsp;f</i><tt>.{@link #toURI() toURI}()).equals(</tt><i>&nbsp;f</i><tt>.{@link #getAbsoluteFile() getAbsoluteFile}())
     * </tt></blockquote>
     *
     * so long as the original abstract pathname, the URI, and the new abstract
     * pathname are all created in (possibly different invocations of) the same
     * Java virtual machine.  This relationship typically does not hold,
     * however, when a <tt>file:</tt> URI that is created in a virtual machine
     * on one operating system is converted into an abstract pathname in a
     * virtual machine on a different operating system.
     *
     * @param  uri
     *         An absolute, hierarchical URI with a scheme equal to
     *         <tt>"file"</tt>, a non-empty path component, and undefined
     *         authority, query, and fragment components
     *
     * @throws  NullPointerException
     *          If <tt>uri</tt> is <tt>null</tt>
     *
     * @throws  IllegalArgumentException
     *          If the preconditions on the parameter do not hold
     *
     * @see #toURI()
     * @see java.net.URI
     * @since 1.4
     */
    public File(URI uri) {
        super(uri);
    }

    /**
     * 通过一个 {@link java.io.File} 对象来构建一个 {@link File} 对象实例，并且参数 File
     * 不能是空的。
     *
     * @param file java.io 下的 File 对象实例
     */
    public File(java.io.File file) {
        this(file.getPath());
    }

    /**
     * 将指定的路径名字符串包装为自定义的 `File` 对象。
     * <p>
     * 该方法接收一个表示文件路径的字符串，并将其包装为当前定义的自定义 `File` 对象，以便进一步处理。
     *
     * @param pathname 要包装的文件路径名字符串。
     * @return 一个新的 `File` 对象，包装了传入的文件路径名字符串。
     */
    public static File wrap(String pathname) {
        return new File(pathname);
    }

    /**
     * 将指定的 `java.io.File` 对象包装为自定义的 `File` 对象。
     * <p>
     * 该方法接收一个标准的 `java.io.File` 对象，并将其包装为当前定义的自定义 `File` 对象，以便进一步处理。
     *
     * @param file 要包装的 `java.io.File` 对象。
     * @return 一个新的 `File` 对象，包装了传入的 `java.io.File` 对象。
     */
    public static File wrap(java.io.File file) {
        return new File(file);
    }

    /**
     * @return 返回一个干净的文件名称，指不带扩展后缀的
     *         文件名
     */
    public String getCleanName() {
        String name = getName();
        return strcut(getName(), 0, name.lastIndexOf("."));
    }

    /**
     * 比较两个文件的扩展类型是否一致。如果扩展名一致的话则返回 `true`。文件
     * 的扩展名需要带 '.'，如：.pdf
     *
     * @param extension 文件扩展名
     * @return 当前 File 文件和 {@code extension} 一致返回 `true`
     */
    public boolean typeEquals(String extension) {
        return streq(extension, getExtension());
    }

    /**
     * #brief: 检查文件扩展名是否匹配
     *
     * 该函数用于检查当前对象的文件扩展名是否匹配指定的扩展名列表。
     *
     * @param extensions
     *        需要匹配的文件扩展名列表，可变参数形式
     *
     * @return 如果当前对象的扩展名与任一指定的扩展名匹配，返回 {@code true}；
     *         否则返回 {@code false}
     */
    public boolean typeMatch(String... extensions) {
        return strhas(getExtension(), extensions);
    }

    /**
     * @return 返回文件扩展名（后缀）
     */
    public String getExtension() {
        String name = getName();
        int index = name.indexOf(".");
        if (index == -1)
            return "";
        return strcut(getName(), index, 0);
    }

    private boolean staticForceDeleteDirectory(File dir) {
        List<File> children = Collects.listMap(dir.listFiles(), File::new);
        for (File child : children) {
            if (child.isFile()) {
                child.forceDeleteFile();
            } else {
                child.forceDeleteDirectory();
            }
        }
        return dir.forceDeleteFile();
    }

    private boolean forceDeleteDirectory() {
        return staticForceDeleteDirectory(this);
    }

    private boolean forceDeleteFile() {
        boolean retval;
        if (!(retval = delete())) {
            System.gc();
            retval = delete();
        }
        return retval;
    }

    /**
     * 强制删除一个文件或目录，文件或目录必须存在，否则将什么都不做。如果是目录
     * 会先递归删除目录下的所有文件，然后再删除目录本身。
     *
     * @return 是否删除成功
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean forceDelete() {
        if (exists())
            return isFile() ? forceDeleteFile() : forceDeleteDirectory();
        return false;
    }

    /**
     * 检查文件是否存在，如果不存在就创建一个，但前提是 {@code enableAutoCreate}
     * 参数必须是 {@code true}。
     */
    private void checkFile(boolean autoCreate) {
        if (autoCreate && !exists())
            throwIfError(this::createNewFile);
        xassert(exists(), "%s （系统找不到指定的文件，文件不存在）", getPath());
    }

    /**
     * 打开一个文件输入流，通过参数 {@code autoCreate} 检查当文件不存在时是否
     * 自动创建一个新的文件。<p>
     *
     * 如果 {@code autoCreate} 为 {@code false} 时，当文件不存在就会抛出一个
     * 运行时断言异常。
     *
     * @param autoCreate 文件不存在时是否创建
     * @return 打开描述符后的文件输入流对象
     */
    private FileByteReader openByteReader(boolean autoCreate) {
        checkFile(autoCreate);
        return throwIfError(() -> new FileByteReader(this));
    }

    /**
     * 打开一个文件输出流，通过参数 {@code autoCreate} 检查当文件不存在时是否
     * 自动创建一个新的文件。<p>
     *
     * 如果 {@code autoCreate} 为 {@code false} 时，当文件不存在就会抛出一个
     * 运行时断言异常。
     *
     * @param autoCreate 文件不存在时是否创建
     * @return 打开描述符后的文件输出流对象
     */
    private FileByteWriter openByteWriter(boolean autoCreate) {
        checkFile(autoCreate);
        return throwIfError(() -> new FileByteWriter(this));
    }

    /**
     * 打开文件输入流，如果文件不存在则会自动创建一个新的空文件。避免当文件
     * 不存在时系统抛出 FileNotFound 异常。
     *
     * @return 打开描述符后的文件输入流对象
     */
    public FileByteReader openByteReader() {
        return openByteReader(true);
    }

    /**
     * 打开文件输出流，如果文件不存在则会自动创建一个新的空文件。避免当文件
     * 不存在时系统抛出 FileNotFound 异常。
     *
     * @return 打开描述符后的文件输入流对象
     */
    public FileByteWriter openByteWriter() {
        return openByteWriter(true);
    }

    /**
     * 打开文件输入流，如果当文件不存在时则会抛出一个 FileNotFound 异常，该函数
     * 不会自动创建文件。
     *
     * @return 打开描述符后的文件输入流对象
     */
    public FileByteReader openByteReaderDisabled() {
        return openByteReader(false);
    }

    /**
     * 打开文件输出流，如果当文件不存在时则会抛出一个 FileNotFound 异常，该函数
     * 不会自动创建文件。
     *
     * @return 打开描述符后的文件输出流对象
     */
    public FileByteWriter openByteWriterDisabled() {
        return openByteWriter(false);
    }

    /**
     * 将整个文件以字符串的形式读取到内存中，并将字符串对象引用返回
     * 出去。
     *
     * @return 文件内容
     */
    public String strread() {
        return IOUtils.strread(openByteReader());
    }

    /**
     * 打开文件描述符，使得 <code>File</code> 文件对象支持系统随机读写
     * 访问。随机读写机制能够更灵活的操作文件内容，并写入。性能更高。
     * 同时当文件打开成功以后，当前对象将支持所有随机读写访问函数。
     * <p>
     *
     * 当调用 <code>open()</code> 函数时，操作系统会打开一个文件描述符，
     * 该描述符由操作系统中的文件系统管理，
     * <p>
     *
     * 默认同时支持读写操作。
     *
     * @return 描述符是否打开成功
     */
    public boolean open() {
        return open("rwd");
    }

    /**
     * 打开文件描述符，使得 <code>File</code> 文件对象支持系统随机读写
     * 访问。随机读写机制能够更灵活的操作文件内容，并写入。性能更高。
     * 同时当文件打开成功以后，当前对象将支持所有随机读写访问函数。
     * <p>
     *
     * 当调用 <code>open()</code> 函数时，操作系统会打开一个文件描述符，
     * 该描述符由操作系统中的文件系统管理，
     *
     * @param mode 打开模式，r 表示支持读取，w 表示支持写入，也可以
     *             连起来写，rw 表示同时支持读取和写入
     *
     * @return 描述符是否打开成功
     */
    public boolean open(String mode) {
        return (accessFile = optionalIfError(() -> new RandomAccessFile(this, mode), null)) != null;
    }

    /**
     * 检查文件描述符是否打开
     */
    private void checkOpen() {
        xassert(accessFile != null, "Please call open() before random access methods execute.");
    }

    /**
     * Attempts to skip over {@code n} bytes of input discarding the
     * skipped bytes.
     * <p>
     *
     * This method may skip over some smaller number of bytes, possibly zero.
     * This may result from any of a number of conditions; reaching end of
     * file before {@code n} bytes have been skipped is only one
     * possibility. This method never throws an {@code EOFException}.
     * The actual number of bytes skipped is returned.  If {@code n}
     * is negative, no bytes are skipped.
     *
     * @param      n   the number of bytes to be skipped.
     * @return     the actual number of bytes skipped.
     */
    public int skipBytes(int n) {
        checkOpen();
        return throwIfError(() -> accessFile.skipBytes(n));
    }

    /**
     * Sets the file-pointer offset, measured from the beginning of this
     * file, at which the next read or write occurs.  The offset may be
     * set beyond the end of the file. Setting the offset beyond the end
     * of the file does not change the file length.  The file length will
     * change only by writing after the offset has been set beyond the end
     * of the file.
     *
     * @param      pos   the offset position, measured in bytes from the
     *                   beginning of the file, at which to set the file
     *                   pointer.
     */
    public void seek(long pos) {
        checkOpen();
        throwIfError(() -> accessFile.seek(pos));
    }

    /**
     * 从当前文件数据中读取一个字节并返回，返回的字节类型时一个 int
     * 类型，int 的取值范围是从 0 - 255，<code>0x00-0x0ff</code>，
     * 这个方法会阻塞执行。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @return 返回一个取值范围从 0-255 的字节数据，如果返回
     *         值等于 <code>-1</code> 那么表示文件已读取到
     *         末尾。
     */
    public int read() {
        checkOpen();
        return throwIfError(() -> accessFile.read());
    }

    /**
     * 从当前文件的数据中读取 <code>b.length</code> 字节到 <code>b</code> 字节
     * 缓冲区数组中，这个方法会阻塞执行。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param b   字节缓冲区数组
     */
    public void read(byte[] b) {
        checkOpen();
        throwIfError(() -> accessFile.read(b));
    }

    /**
     * 从当前文件的数据中读取 <code>len</code> 字节到 <code>b</code> 字节
     * 缓冲区数组中，这个方法会阻塞执行。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param b   字节缓冲区数组
     * @param off 缓冲区偏移量，表示从 <code>b</code> 缓冲区的 <code>off</code>
     *            位置开始写入。
     * @param len 数据读取长度，表示要从文件中读取 <code>len</code> 个字节
     *            数据到字节缓冲区中。
     */
    public void read(byte[] b, int off, int len) {
        checkOpen();
        throwIfError(() -> accessFile.read(b, off, len));
    }

    /**
     * 从当前访问偏移量的位置在文件数据中往后读取 {@link Integer#BYTES} 个
     * 字节数据，并将读取到的字节数据转换成一个 int 类型的数据。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @return 返回从文件中读取到的 int 数据
     */
    public int readInt() {
        checkOpen();
        return throwIfError(() -> accessFile.readInt());
    }

    /**
     * 从当前访问偏移量的位置在文件数据中往后读取 {@link Long#BYTES} 个
     * 字节数据，并将读取到的字节数据转换成一个 long 类型的数据类。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @return 返回从文件中读取到的 long 数据
     */
    public long readLong() {
        checkOpen();
        return throwIfError(() -> accessFile.readLong());
    }

    /**
     * 从当前访问偏移量的位置在文件数据中往后读取 {@link Float#BYTES} 个
     * 字节数据，并将读取到的字节数据转换成一个 float 类型的数据类。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @return 返回从文件中读取到的 float 数据
     */
    public float readFloat() {
        checkOpen();
        return throwIfError(() -> accessFile.readFloat());
    }

    /**
     * 从当前访问偏移量的位置在文件数据中往后读取 {@link Double#BYTES} 个
     * 字节数据，并将读取到的字节数据转换成一个 double 类型的数据类。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @return 返回从文件中读取到的 double 数据
     */
    public double readDouble() {
        checkOpen();
        return throwIfError(() -> accessFile.readDouble());
    }

    /**
     * 写入一个字节数据到当前的文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param b 需要写入的字节数据
     */
    public void write(byte b) {
        checkOpen();
        throwIfError(() -> accessFile.write(b));
    }

    /**
     * 写入一个字节数组数据到当前的文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param b 需要写入的字节数组
     */
    public void write(byte[] b) {
        checkOpen();
        throwIfError(() -> accessFile.write(b));
    }

    /**
     * 根据提供的偏移量和长度写入将字节数组中的数据写入到当前
     * 文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param b   字节缓冲区数组
     * @param off 缓冲区偏移量，表示从 <code>b</code> 缓冲区的 <code>off</code>
     *            位置开始写入。
     * @param len 数据写入长度，表示要从字节缓冲区中读取 <code>len</code> 个字节
     *            数据文件中。
     */
    public void write(byte[] b, int off, int len) {
        checkOpen();
        throwIfError(() -> accessFile.write(b, off, len));
    }

    /**
     * 写入一个 Integer 数据到文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param value 需要写入的 Integer 数据。
     */
    public void writeInt(int value) {
        checkOpen();
        throwIfError(() -> accessFile.writeInt(value));
    }

    /**
     * 写入一个 Long 数据到文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param value 需要写入的 Long 数据。
     */
    public void writeLong(long value) {
        checkOpen();
        throwIfError(() -> accessFile.writeLong(value));
    }

    /**
     * 写入一个 Float 数据到文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param value 需要写入的 Float 数据。
     */
    public void writeFloat(float value) {
        checkOpen();
        throwIfError(() -> accessFile.writeFloat(value));
    }

    /**
     * 写入一个 Double 数据到文件中。
     * <p>
     * 使用随机读写函数前，请务必先打开文件随机读写访问对象，使用
     * <code>open()</code> 函数打开随机读写访问对象，否则禁止使用
     * 随机读写函数。
     *
     * @param value 需要写入的 Double 数据。
     */
    public void writeDouble(double value) {
        checkOpen();
        throwIfError(() -> accessFile.writeDouble(value));
    }

    /**
     * 关闭文件描述符，当文件描述符被关闭后，<code>File</code> 对象将
     * 不再支持随机读写访问，并释放出文件描述符句柄。如果需要重新使用
     * 随机读写访问功能，重新打开文件描述符即可，
     */
    public void close() {
        checkOpen();
        IOUtils.closeQuietly(accessFile);
        accessFile = null;
    }

}