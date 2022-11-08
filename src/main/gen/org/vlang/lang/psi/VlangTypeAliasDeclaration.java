// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import org.vlang.lang.stubs.VlangTypeAliasDeclarationStub;
import com.intellij.psi.ResolveState;

public interface VlangTypeAliasDeclaration extends VlangNamedElement, VlangAttributeOwner, StubBasedPsiElement<VlangTypeAliasDeclarationStub> {

  @Nullable
  VlangAliasType getAliasType();

  @Nullable
  VlangAttributes getAttributes();

  @Nullable
  VlangSymbolVisibility getSymbolVisibility();

  @NotNull
  PsiElement getType_();

  @Nullable
  PsiElement getIdentifier();

  @NotNull
  String getName();

  @Nullable
  VlangType getTypeInner(@Nullable ResolveState context);

}
