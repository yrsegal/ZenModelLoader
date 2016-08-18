package wiresegal.zenmodelloader.common.core

import net.minecraftforge.fml.relauncher.ReflectionHelper
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles.publicLookup

/**
 * @author WireSegal
 * Created at 6:49 PM on 8/14/16.
 */
object MethodHandleHelper {

    data class NullableInvocationWrapper<out T>(val wrapped: (Array<out Any>) -> T?) {
        operator fun invoke(vararg args: Any) = wrapped(args)
    }

    data class NonnullInvocationWrapper<out T>(val wrapped: (Array<out Any>) -> T) {
        operator fun invoke(vararg args: Any) = wrapped(args)
    }

    /**
     * Reflects a method from a class, and provides a MethodHandle for it. This is less simple to use than an accessor but might be more useful.
     */
    @JvmStatic
    fun <T> handleForMethod(clazz: Class<T>, names: Array<String>, vararg methodClasses: Class<*>): MethodHandle {
        val m = ReflectionHelper.findMethod<T>(clazz, null, names, *methodClasses)
        return publicLookup().unreflect(m)
    }

    /**
     * Creates an accessor for a reflected method returning a nonnull object. Accessors are merely a convenient wrapper.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <O, R> accessorForNonnullMethod(clazz: Class<O>, returnType: Class<R>, names: Array<String>, vararg methodClasses: Class<*>): NonnullInvocationWrapper<R> {
        val m = ReflectionHelper.findMethod<O>(clazz, null, names, *methodClasses)
        if (m.returnType != returnType) throw ClassCastException("Found return type ${m.returnType} is not expected type $returnType")
        val handle = publicLookup().unreflect(m)
        return NonnullInvocationWrapper { handle.invokeExact(*it) as R }
    }

    /**
     * Creates an accessor for a reflected method returning a nullable object. Accessors are merely a convenient wrapper.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <O, R> accessorForNullableMethod(clazz: Class<O>, returnType: Class<R>, names: Array<String>, vararg methodClasses: Class<*>): NullableInvocationWrapper<R> {
        val m = ReflectionHelper.findMethod<O>(clazz, null, names, *methodClasses)
        if (m.returnType != returnType) throw ClassCastException("Found return type ${m.returnType} is not expected type $returnType")
        val handle = publicLookup().unreflect(m)
        return NullableInvocationWrapper { handle.invokeExact(*it) as R? }
    }

    /**
     * Reflects a field from a class, and provides a MethodHandle for it. This is less simple to use than an accessor but might be more useful.
     */
    @JvmStatic
    fun <T> handleForField(clazz: Class<T>, getter: Boolean, vararg names: String): MethodHandle {
        val f = ReflectionHelper.findField(clazz, *names)
        return if (getter) publicLookup().unreflectGetter(f) else publicLookup().unreflectSetter(f)
    }

    /**
     * Creates an getter accessor for a reflected field containing a nonnull object. Accessors are merely a convenient wrapper.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <O, R> accessorForNonnullGetter(clazz: Class<O>, fieldType: Class<R>, vararg names: String): NonnullInvocationWrapper<R> {
        val m = ReflectionHelper.findField(clazz, *names)
        if (m.type != fieldType) throw ClassCastException("Found field type ${m.type} is not expected type $fieldType")
        val handle = publicLookup().unreflectGetter(m)
        return NonnullInvocationWrapper { handle.invokeExact(*it) as R }
    }

    /**
     * Creates an getter accessor for a reflected field containing a nullable object. Accessors are merely a convenient wrapper.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <O, R> accessorForNullableGetter(clazz: Class<O>, fieldType: Class<R>, vararg names: String): NullableInvocationWrapper<R> {
        val m = ReflectionHelper.findField(clazz, *names)
        if (m.type != fieldType) throw ClassCastException("Found field type ${m.type} is not expected type $fieldType")
        val handle = publicLookup().unreflectGetter(m)
        return NullableInvocationWrapper { handle.invokeExact(*it) as R? }
    }

    /**
     * Creates a setter accessor for a reflected field. Accessors are merely a convenient wrapper.
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> accessorForSetter(clazz: Class<T>, vararg names: String): NullableInvocationWrapper<Unit> {
        val m = ReflectionHelper.findField(clazz, *names)
        val handle = publicLookup().unreflectSetter(m)
        return NullableInvocationWrapper { handle.invokeExact(*it) as Unit }
    }
}
