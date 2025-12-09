# Types

###### Base

NativePtr - typealias NativePtr = kotlin.native.internal.NativePtr

###### Heap

NativePlacement - interface NativePlacement
NativeFreeablePlacement - interface NativeFreeablePlacement : NativePlacement
nativeHeap - object nativeHeap : NativeFreeablePlacement

###### Scope

DeferScope - open class DeferScope
AutofreeScope - abstract class AutofreeScope : DeferScope, NativePlacement
ArenaBase - open class ArenaBase(parent: NativeFreeablePlacement = nativeHeap) : AutofreeScope
Arena - class Arena(parent: NativeFreeablePlacement = nativeHeap) : ArenaBase
MemScope - class MemScope : ArenaBase

###### Types

CEnum - interface CEnum
Vector128 - class Vector128

###### Types/Pointed

NativePointed - open class NativePointed

###### Types/CPointed

CPointed - abstract class CPointed(rawPtr: NativePtr) : NativePointed
CVariable - abstract class CVariable(rawPtr: NativePtr) : CPointed
CPrimitiveVar - sealed class CPrimitiveVar(rawPtr: NativePtr) : CVariable
CFunction - class CFunction<T : Function<*>>(rawPtr: NativePtr) : CPointed
COpaque - abstract class COpaque(rawPtr: NativePtr) : CPointed

###### Types/CValuesRef

CValuesRef - abstract class CValuesRef<T : CPointed>
CValue - abstract class CValue<T : CVariable> : CValues<T>
CValues - abstract class CValues<T : CVariable> : CValuesRef<T>
CPointer - class CPointer<T : CPointed> : CValuesRef<T>
CArrayPointer - typealias CArrayPointer<T> = CPointer<T>
COpaquePointer - typealias COpaquePointer = CPointer<out CPointed>

###### Types/CVariable

CStructVar - abstract class CStructVar(rawPtr: NativePtr) : CVariable
Vector128VarOf - class Vector128VarOf<T : Vector128>(rawPtr: NativePtr) : CVariable
CPointerVarOf - class CPointerVarOf<T : CPointer<*>>(rawPtr: NativePtr) : CVariable

###### Types/CVariable

Vector128Var - typealias Vector128Var = Vector128VarOf<Vector128>
CPointerVar - typealias CPointerVar<T> = CPointerVarOf<CPointer<T>>
CArrayPointerVar - typealias CArrayPointerVar<T> = CPointerVar<T>
COpaquePointerVar - typealias COpaquePointerVar = CPointerVarOf<COpaquePointer>

###### Types/CVariable/Primitive

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

###### Types/CVariable/Primitive

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

###### Types/Objective-C

ObjCObject - interface ObjCObject
ObjCObjectMeta - typealias ObjCObjectMeta = ObjCClass
ObjCClass - interface ObjCClass : ObjCObject
ObjCClassOf - interface ObjCClassOf<T : ObjCObject> : ObjCClass
ObjCProtocol - interface ObjCProtocol : ObjCObject

###### Types/Objective-C

ObjCObjectBase - abstract class ObjCObjectBase : ObjCObject
ObjCObjectBaseMeta - abstract class ObjCObjectBaseMeta : ObjCObjectBase, ObjCClass

###### Types/Objective-C

ObjCNotImplementedVar - class ObjCNotImplementedVar<T>(rawPtr: NativePtr) : CVariable
ObjCBlockVar - typealias ObjCBlockVar<T> = ObjCNotImplementedVar<T>
ObjCStringVarOf - typealias ObjCStringVarOf<T> = ObjCNotImplementedVar<T>
ObjCObjectVar - class ObjCObjectVar<T>(rawPtr: NativePtr) : CVariable

###### Utils

Pinned - class Pinned<T : Any>
StableRef - @JvmInline value class StableRef<out T : Any>

###### Exceptions

ForeignException - class ForeignException : Exception

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
