package org.vlang.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.impl.light.LightElement
import org.vlang.lang.psi.*
import org.vlang.lang.psi.impl.VlangPsiImplUtil.getUnderlyingType
import org.vlang.lang.psi.impl.VlangPsiImplUtil.resolveType
import org.vlang.lang.stubs.VlangTypeStub

abstract class VlangLightType<E : VlangCompositeElement>(
    protected val element: E,
) : LightElement(element.manager, element.language), VlangType {

    init {
        setNavigationElement(element)
    }

    override fun getContainingFile(): PsiFile = element.containingFile

    override fun getTypeReferenceExpression() = null

    override fun getIdentifier() = null

    override fun toString() = javaClass.simpleName + "{" + element + "}"

    override fun getElementType() = null

    override fun getStub(): VlangTypeStub? = null

    override fun getGenericArguments() = null

    override fun getUnderlyingType(): VlangType? = getUnderlyingType(this)

    override fun resolveType() = resolveType(this)

    class LightArrayType(type: VlangType) : VlangLightType<VlangType>(type), VlangArrayType {
        override fun getText() = "[]" + element.text

        override fun getType() = element

        override fun getLbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "["
                }

                override fun getParent(): PsiElement {
                    return this@LightArrayType
                }
            }
        }

        override fun getRbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "]"
                }

                override fun getParent(): PsiElement {
                    return this@LightArrayType
                }
            }
        }
    }

    class LightMapType(private val keyType: VlangType, valueType: VlangType) : VlangLightType<VlangType>(valueType), VlangMapType {
        override fun getText() = "[]" + element.text

        override fun getType() = element

        override fun getTypeList() = mutableListOf(keyType, element)

        override fun getKeyType() = keyType

        override fun getValueType() = element

        override fun getLbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "["
                }

                override fun getParent(): PsiElement {
                    return this@LightMapType
                }
            }
        }

        override fun getRbrack(): PsiElement {
            return object : FakePsiElement() {
                override fun getText(): String {
                    return "]"
                }

                override fun getParent(): PsiElement {
                    return this@LightMapType
                }
            }
        }
    }

    class LightPointerType(type: VlangType) : VlangLightType<VlangType>(type), VlangPointerType {
        override fun getText() = "&" + element.text

        override fun getType() = element
    }

    class LightFunctionType(o: VlangSignatureOwner) : VlangLightType<VlangSignatureOwner>(o), VlangFunctionType {
        override fun getSignature() = element.getSignature()

        override fun getFn(): PsiElement {
            return if (element is VlangFunctionOrMethodDeclaration)
                (element as VlangFunctionOrMethodDeclaration).getFn()
            else
                element
        }

        override fun getText(): String {
            val signature = element.getSignature()
            return "fn " + if (signature != null) signature.text else "<null>"
        }

        override fun getType() = null
    }
}
