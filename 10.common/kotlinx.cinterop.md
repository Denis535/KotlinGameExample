# Base
NativePointed - open class NativePointed
NativePtr - typealias NativePtr = kotlin.native.internal.NativePtr
# Base/Heap
NativePlacement - interface NativePlacement
NativeFreeablePlacement - interface NativeFreeablePlacement : NativePlacement
nativeHeap - object nativeHeap : NativeFreeablePlacement

# Scope
DeferScope - open class DeferScope
AutofreeScope - abstract class AutofreeScope : DeferScope, NativePlacement
ArenaBase - open class ArenaBase(parent: NativeFreeablePlacement = nativeHeap) : AutofreeScope
Arena - class Arena(parent: NativeFreeablePlacement = nativeHeap) : ArenaBase
MemScope - class MemScope : ArenaBase

# Types
CEnum - interface CEnum
Vector128 - class Vector128
# Types/CValuesRef
CValuesRef - abstract class CValuesRef<T : CPointed>
CValues - abstract class CValues<T : CVariable> : CValuesRef<T>
CValue - abstract class CValue<T : CVariable> : CValues<T>
CPointer - class CPointer<T : CPointed> : CValuesRef<T>
COpaquePointer - typealias COpaquePointer = CPointer<out CPointed>
CArrayPointer - typealias CArrayPointer<T> = CPointer<T>
# Types/CPointed
CPointed - abstract class CPointed(rawPtr: NativePtr) : NativePointed
COpaque - abstract class COpaque(rawPtr: NativePtr) : CPointed
CFunction - class CFunction<T : Function<*>>(rawPtr: NativePtr) : CPointed
# Types/CVariable
CVariable - abstract class CVariable(rawPtr: NativePtr) : CPointed
Vector128VarOf - class Vector128VarOf<T : Vector128>(rawPtr: NativePtr) : CVariable
CPointerVarOf - class CPointerVarOf<T : CPointer<*>>(rawPtr: NativePtr) : CVariable
CStructVar - abstract class CStructVar(rawPtr: NativePtr) : CVariable
# Types/CVariable
Vector128Var - typealias Vector128Var = Vector128VarOf<Vector128>
CPointerVar - typealias CPointerVar<T> = CPointerVarOf<CPointer<T>>
COpaquePointerVar - typealias COpaquePointerVar = CPointerVarOf<COpaquePointer>
CArrayPointerVar - typealias CArrayPointerVar<T> = CPointerVar<T>
# Types/CPrimitiveVar
CPrimitiveVar - sealed class CPrimitiveVar(rawPtr: NativePtr) : CVariable
BooleanVarOf - class BooleanVarOf<T : Boolean>(rawPtr: NativePtr) : CPrimitiveVar
ByteVarOf - class ByteVarOf<T : Byte>(rawPtr: NativePtr) : CPrimitiveVar
ShortVarOf - class ShortVarOf<T : Short>(rawPtr: NativePtr) : CPrimitiveVar
IntVarOf - class IntVarOf<T : Int>(rawPtr: NativePtr) : CPrimitiveVar
LongVarOf - class LongVarOf<T : Long>(rawPtr: NativePtr) : CPrimitiveVar
UByteVarOf - class UByteVarOf<T : UByte>(rawPtr: NativePtr) : CPrimitiveVar
UShortVarOf - class UShortVarOf<T : UShort>(rawPtr: NativePtr) : CPrimitiveVar
UIntVarOf - class UIntVarOf<T : UInt>(rawPtr: NativePtr) : CPrimitiveVar
ULongVarOf - class ULongVarOf<T : ULong>(rawPtr: NativePtr) : CPrimitiveVar
FloatVarOf - class FloatVarOf<T : Float>(rawPtr: NativePtr) : CPrimitiveVar
DoubleVarOf - class DoubleVarOf<T : Double>(rawPtr: NativePtr) : CPrimitiveVar
CEnumVar - abstract class CEnumVar(rawPtr: NativePtr) : CPrimitiveVar
# Types/CPrimitiveVar
BooleanVar - typealias BooleanVar = BooleanVarOf<Boolean>
ByteVar - typealias ByteVar = ByteVarOf<Byte>
ShortVar - typealias ShortVar = ShortVarOf<Short>
IntVar - typealias IntVar = IntVarOf<Int>
LongVar - typealias LongVar = LongVarOf<Long>
UByteVar - typealias UByteVar = UByteVarOf<UByte>
UShortVar - typealias UShortVar = UShortVarOf<UShort>
UIntVar - typealias UIntVar = UIntVarOf<UInt>
ULongVar - typealias ULongVar = ULongVarOf<ULong>
FloatVar - typealias FloatVar = FloatVarOf<Float>
DoubleVar - typealias DoubleVar = DoubleVarOf<Double>
# Types/Objective-C
ObjCObject - interface ObjCObject
ObjCObjectMeta - typealias ObjCObjectMeta = ObjCClass
ObjCClass - interface ObjCClass : ObjCObject
ObjCClassOf - interface ObjCClassOf<T : ObjCObject> : ObjCClass
ObjCProtocol - interface ObjCProtocol : ObjCObject
ObjCObjectBase - abstract class ObjCObjectBase : ObjCObject
ObjCObjectBaseMeta - abstract class ObjCObjectBaseMeta : ObjCObjectBase, ObjCClass
ObjCNotImplementedVar - class ObjCNotImplementedVar<T>(rawPtr: NativePtr) : CVariable
ObjCBlockVar - typealias ObjCBlockVar<T> = ObjCNotImplementedVar<T>
ObjCStringVarOf - typealias ObjCStringVarOf<T> = ObjCNotImplementedVar<T>
ObjCObjectVar - class ObjCObjectVar<T>(rawPtr: NativePtr) : CVariable

# Utils
Pinned - class Pinned<T : Any>
StableRef - @JvmInline value class StableRef<out T : Any>

# Exceptions
ForeignException - class ForeignException : Exception

# Annotations/Interop
@Target(allowedTargets = [AnnotationTarget.FILE])
annotation class InteropStubs

@Target(allowedTargets = [AnnotationTarget.TYPEALIAS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS])
annotation class BetaInteropApi

@Target(allowedTargets = [AnnotationTarget.TYPEALIAS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS])
annotation class ExperimentalForeignApi

@Target(allowedTargets = [AnnotationTarget.TYPEALIAS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY])
annotation class UnsafeNumber(val actualPlatformTypes: Array<String>)

# Annotations/Objective-C/Classes
@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class ExportObjCClass(val name: String = "")

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class ExternalObjCClass(val protocolGetter: String = "", val binaryName: String = "")

# Annotations/Objective-C/Properties
@Target(allowedTargets = [AnnotationTarget.PROPERTY])
annotation class ObjCOutlet

# Annotations/Objective-C/Functions
@Target(allowedTargets = [AnnotationTarget.CONSTRUCTOR])
annotation class ObjCConstructor(val initSelector: String, val designated: Boolean)

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class ObjCAction

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class ObjCDirect(val symbol: String)

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class ObjCFactory(val selector: String, val encoding: String, val isStret: Boolean = false)

@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER])
annotation class ObjCMethod(val selector: String, val encoding: String, val isStret: Boolean = false)

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class ObjCSignatureOverride
