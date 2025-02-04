// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.vlang.lang.psi.VlangPsiTreeUtil;
import static org.vlang.lang.VlangTypes.*;
import org.vlang.lang.psi.*;

public class VlangTypeReferenceExpressionImpl extends VlangCompositeElementImpl implements VlangTypeReferenceExpression {

  public VlangTypeReferenceExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VlangVisitor visitor) {
    visitor.visitTypeReferenceExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VlangVisitor) accept((VlangVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  @NotNull
  public VlangReference getReference() {
    return VlangPsiImplUtil.getReference(this);
  }

  @Override
  @Nullable
  public VlangCompositeElement getQualifier() {
    return VlangPsiImplUtil.getQualifier(this);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    return VlangPsiImplUtil.resolve(this);
  }

}
