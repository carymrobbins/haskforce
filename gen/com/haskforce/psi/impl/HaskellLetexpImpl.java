// This is a generated file. Not intended for manual editing.
package com.haskforce.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.haskforce.psi.HaskellTypes.*;
import com.haskforce.psi.*;

public class HaskellLetexpImpl extends HaskellCompositeElementImpl implements HaskellLetexp {

  public HaskellLetexpImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull HaskellVisitor visitor) {
    visitor.visitLetexp(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof HaskellVisitor) accept((HaskellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public HaskellExp getExp() {
    return PsiTreeUtil.getChildOfType(this, HaskellExp.class);
  }

  @Override
  @NotNull
  public List<HaskellFunorpatdecl> getFunorpatdeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellFunorpatdecl.class);
  }

  @Override
  @NotNull
  public List<HaskellGendecl> getGendeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellGendecl.class);
  }

  @Override
  @NotNull
  public List<HaskellPpragma> getPpragmaList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HaskellPpragma.class);
  }

  @Override
  @Nullable
  public PsiElement getIn() {
    return findChildByType(IN);
  }

  @Override
  @NotNull
  public PsiElement getLet() {
    return notNullChild(findChildByType(LET));
  }

  @Override
  @Nullable
  public PsiElement getWhitespacelbracetok() {
    return findChildByType(WHITESPACELBRACETOK);
  }

  @Override
  @Nullable
  public PsiElement getWhitespacerbracetok() {
    return findChildByType(WHITESPACERBRACETOK);
  }

  @Override
  @Nullable
  public PsiElement getLbrace() {
    return findChildByType(LBRACE);
  }

  @Override
  @Nullable
  public PsiElement getRbrace() {
    return findChildByType(RBRACE);
  }

}
