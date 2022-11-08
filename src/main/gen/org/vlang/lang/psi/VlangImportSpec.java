// This is a generated file. Not intended for manual editing.
package org.vlang.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VlangImportSpec extends VlangCompositeElement {

  @Nullable
  VlangImportAlias getImportAlias();

  @NotNull
  VlangImportPath getImportPath();

  @Nullable
  VlangSelectiveImportList getSelectiveImportList();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  String getLastPart();

  @NotNull
  PsiElement getLastPartPsi();

  @NotNull
  String getName();

  @NotNull
  String getImportedName();

}
