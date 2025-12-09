# Types

###### Base

typealias NativePtr = kotlin.native.internal.NativePtr

###### Heap

interface NativePlacement
interface NativeFreeablePlacement : NativePlacement
object nativeHeap : NativeFreeablePlacement

###### Scope

open class DeferScope
abstract class AutofreeScope : DeferScope, NativePlacement
open class ArenaBase(parent: NativeFreeablePlacement = nativeHeap) : AutofreeScope
class Arena(parent: NativeFreeablePlacement = nativeHeap) : ArenaBase
class MemScope : ArenaBase

###### PinnedRef

Pinned - class Pinned<T : Any>

###### StableRef

StableRef - @JvmInline value class StableRef<out T : Any>

###### Types

interface CEnum
class Vector128

###### Types/Pointed

open class NativePointed

###### Types/CPointed

abstract class CPointed(rawPtr: NativePtr) : NativePointed
abstract class CVariable(rawPtr: NativePtr) : CPointed
sealed class CPrimitiveVar(rawPtr: NativePtr) : CVariable
class CFunction<T : Function<*>>(rawPtr: NativePtr) : CPointed
abstract class COpaque(rawPtr: NativePtr) : CPointed

###### Types/CValuesRef

abstract class CValuesRef<T : CPointed>
abstract class CValue<T : CVariable> : CValues<T>
abstract class CValues<T : CVariable> : CValuesRef<T>
class CPointer<T : CPointed> : CValuesRef<T>
typealias CArrayPointer<T> = CPointer<T>
typealias COpaquePointer = CPointer<out CPointed>

###### Types/CVariable

abstract class CStructVar(rawPtr: NativePtr) : CVariable
class Vector128VarOf<T : Vector128>(rawPtr: NativePtr) : CVariable
class CPointerVarOf<T : CPointer<*>>(rawPtr: NativePtr) : CVariable

###### Types/CVariable

typealias Vector128Var = Vector128VarOf<Vector128>
typealias CPointerVar<T> = CPointerVarOf<CPointer<T>>
typealias CArrayPointerVar<T> = CPointerVar<T>
typealias COpaquePointerVar = CPointerVarOf<COpaquePointer>

###### Types/CVariable/Primitive

class BooleanVarOf<T : Boolean>(rawPtr: NativePtr) : CPrimitiveVar
class ByteVarOf<T : Byte>(rawPtr: NativePtr) : CPrimitiveVar
class ShortVarOf<T : Short>(rawPtr: NativePtr) : CPrimitiveVar
class IntVarOf<T : Int>(rawPtr: NativePtr) : CPrimitiveVar
class LongVarOf<T : Long>(rawPtr: NativePtr) : CPrimitiveVar
class UByteVarOf<T : UByte>(rawPtr: NativePtr) : CPrimitiveVar
class UShortVarOf<T : UShort>(rawPtr: NativePtr) : CPrimitiveVar
class UIntVarOf<T : UInt>(rawPtr: NativePtr) : CPrimitiveVar
class ULongVarOf<T : ULong>(rawPtr: NativePtr) : CPrimitiveVar
class FloatVarOf<T : Float>(rawPtr: NativePtr) : CPrimitiveVar
class DoubleVarOf<T : Double>(rawPtr: NativePtr) : CPrimitiveVar
abstract class CEnumVar(rawPtr: NativePtr) : CPrimitiveVar

###### Types/CVariable/Primitive

typealias BooleanVar = BooleanVarOf<Boolean>
typealias ByteVar = ByteVarOf<Byte>
typealias ShortVar = ShortVarOf<Short>
typealias IntVar = IntVarOf<Int>
typealias LongVar = LongVarOf<Long>
typealias UByteVar = UByteVarOf<UByte>
typealias UShortVar = UShortVarOf<UShort>
typealias UIntVar = UIntVarOf<UInt>
typealias ULongVar = ULongVarOf<ULong>
typealias FloatVar = FloatVarOf<Float>
typealias DoubleVar = DoubleVarOf<Double>

###### Types/Objective-C

interface ObjCObject
typealias ObjCObjectMeta = ObjCClass
interface ObjCClass : ObjCObject
interface ObjCClassOf<T : ObjCObject> : ObjCClass
interface ObjCProtocol : ObjCObject

###### Types/Objective-C

abstract class ObjCObjectBase : ObjCObject
abstract class ObjCObjectBaseMeta : ObjCObjectBase, ObjCClass

###### Types/Objective-C

class ObjCNotImplementedVar<T>(rawPtr: NativePtr) : CVariable
typealias ObjCBlockVar<T> = ObjCNotImplementedVar<T>
typealias ObjCStringVarOf<T> = ObjCNotImplementedVar<T>
class ObjCObjectVar<T>(rawPtr: NativePtr) : CVariable

###### Exceptions

class ForeignException : Exception

###### Annotations/Interop

@Target(allowedTargets = [AnnotationTarget.FILE])
annotation class InteropStubs

@Target(allowedTargets = [AnnotationTarget.TYPEALIAS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS])
annotation class BetaInteropApi

@Target(allowedTargets = [AnnotationTarget.TYPEALIAS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS])
annotation class ExperimentalForeignApi

@Target(allowedTargets = [AnnotationTarget.TYPEALIAS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY])
annotation class UnsafeNumber(val actualPlatformTypes: Array<String>)

###### Annotations/Objective-C/Classes

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class ExportObjCClass(val name: String = "")

@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class ExternalObjCClass(val protocolGetter: String = "", val binaryName: String = "")

###### Annotations/Objective-C/Properties

@Target(allowedTargets = [AnnotationTarget.PROPERTY])
annotation class ObjCOutlet

###### Annotations/Objective-C/Functions

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

# Methods

###### Memory

alloc
allocArray
allocArrayOf
allocArrayOfPointersTo
allocPointerTo
free

###### CPointed

getRawPointer

###### CValue

copy

###### CValues

getBytes

###### CVariable

sizeOf
alignOf

###### CPointer

getRawValue
get
set
memberAt
arrayMemberAt
plus

###### Creation

zeroValue
cValue
createValues
vectorOf
cValuesOf
staticCFunction

###### Utils/Scope

memScoped
usePinned
useContents

###### Utils/Conversion

reinterpret
interpretPointed
interpretNullablePointed
interpretOpaquePointed
interpretNullableOpaquePointed
interpretCPointer

###### Utils/Conversion

toBoolean
toByte
toLong
toKString
toKStringFromUtf8
toKStringFromUtf16
toKStringFromUtf32
toCValues
toCStringArray
toCPointer
convert
bitsToFloat
bitsToDouble

###### Utils/Reading

readBits
readBytes
readValue
readValues

###### Utils/Writing

write
writeBits

###### Utils/Misc

refTo
placeTo

###### Utils/Misc

invoke

###### Kotlin

createKotlinObjectHolder
unwrapKotlinObjectHolder
getOriginalKotlinClass

###### Kotlin/PinnedRef

pin
addressOf

###### Kotlin/StableRef

asStableRef

###### Objective-C

objcPtr

###### Objective-C/Memory

objc_retain
objc_release
objc_retainAutoreleaseReturnValue

###### Objective-C/Memory

autoreleasepool
objc_autoreleasePoolPush
objc_autoreleasePoolPop

###### Objective-C/Conversion

interpretObjCPointer
interpretObjCPointerOrNull

# Properties

val nativeNullPtr: NativePtr

val NativePointed?.rawPtr: NativePtr
val CPointer<*>?.rawValue: NativePtr

val <T : CPointed> T.ptr: CPointer<T>

var <T : CPointed, P : CPointer<T>> CPointerVarOf<P>.pointed: T?
val <T : CPointed> CPointer<T>.pointed: T

var <T : Boolean> BooleanVarOf<T>.value: T
var <T : Byte> ByteVarOf<T>.value: T
var <T : Short> ShortVarOf<T>.value: T
var <T : Int> IntVarOf<T>.value: T
var <T : Long> LongVarOf<T>.value: T
var <T : UByte> UByteVarOf<T>.value: T
var <T : UShort> UShortVarOf<T>.value: T
var <T : UInt> UIntVarOf<T>.value: T
var <T : ULong> ULongVarOf<T>.value: T
var <T : Float> FloatVarOf<T>.value: T
var <T : Double> DoubleVarOf<T>.value: T

var <P : CPointer<*>> CPointerVarOf<P>.value: P?
var <T : Vector128> Vector128VarOf<T>.value: T

var <T> ObjCNotImplementedVar<T>.value: T
var <T> ObjCObjectVar<T>.value: T

val String.cstr: CValues<ByteVar>
val String.wcstr: CValues<UShortVar>

val String.utf8: CValues<ByteVar>
val String.utf16: CValues<UShortVar>
val String.utf32: CValues<IntVar>
