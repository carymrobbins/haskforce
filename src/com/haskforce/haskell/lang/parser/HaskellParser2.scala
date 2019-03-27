package com.haskforce.haskell.lang.parser

import com.intellij.lang.{ASTNode, PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import scalaz.syntax.monad._

final class HaskellParser2 extends PsiParser {
  override def parse(root: IElementType, builder: PsiBuilder): ASTNode = ???
}

object HaskellParser2Parsec {

  import com.haskforce.psi.HaskellTypes._
  import com.haskforce.utils.parser.PsiParsec._

  type P0 = Psi[Unit]

  val pModuleName: P0 = markStart(
    pif(maybeTokenOneOfAdvance(CONID, QCONID))
      .pthen(markDone(??? /*MODULE_NAME*/))
      .pelse(markError("Missing module name"))
  )

  val pModule: P0 =
    whenTokenIs(_ == MODULETOKEN)(markStart(
         advanceLexer
      *> pModuleName
      *> expectTokenAdvance(WHERE)
      *> expectTokenOneOfAdvance(LBRACE /*, VIRTUAL_LBRACE*/)
      *> markDone(MODULEDECL)
    ))

  val pImportModule: P0 = markStart(
    pif(maybeTokenOneOfAdvance(CONID, QCON))
      .pthen(markDone(??? /*IMPORT_MODULE*/))
      .pelse(markError("Missing import module"))
  )

  def pImportName(node: IElementType): P0 =
    getTokenType.map(_.contains(LPAREN)).flatMap(hasParen =>
         advanceLexer.whenM(hasParen)
      *> withTokenType(t =>
           pif(pure(List(CONID, VARID, CONSYM, VARSYM).contains(t))).pthen(
                  markStart(advanceLexer *> markDone(node))
               *> pImportCtorsOrMethods(node)
             ).pelse(error("Invalid explicit import; expected id, constructor, or symbol"))
           *> expectTokenAdvance(RPAREN).whenM(hasParen)
        )
    )

  def pImportCtorsOrMethods(node: IElementType): P0 = ???

  def pImportNames(node: IElementType): P0 = {

    lazy val loop = getTokenType.flatMap(mt =>
      if (mt.contains(LPAREN)) advanceLexer
      else if (mt.contains(COMMA)) advanceLexer *> pImportName(node) *> loop
      else error("Expected close parent or comma in explicit name import")
    )

    getTokenType.flatMap(mt =>
      if (mt.contains(LPAREN)) advanceLexer
      else pImportName(node) *> loop
    )
  }

  val pImport: P0 = markStart(
       advanceLexer
    *> whenTokenIs(_ == QUALIFIED)(advanceLexer)
    *> pImportModule
    *> whenTokenIs(_ == AS)(
            advanceLexer
         *> markStart(
                 expectTokenAdvance(CONID)
              *> markDone(???/*IMPORT_ALIAS*/)
            )
      )
    *> whenTokenIs(_ == LPAREN)(
            advanceLexer
         *> markStart(
              // Also handles the RPAREN
              pImportNames(??? /*IMPORT_EXPLICIT*/)
           *> markDone(???/*IMPORT_EXPLICITS*/)
         )
       )
    *> whenTokenIs(_ == HIDING)(
            advanceLexer
         *> expectTokenAdvance(LPAREN)
         *> markStart(
           pImportNames(???/*IMPORT_HIDDEN*/)
           *> markDone(???/*IMPORT_HIDDENS*/)
         )
       )
    *> markDone(IMPORTT)
  )

  val pImports: P0 = {
    lazy val loop = (
      pImport
      *> whenTokenIs(_ == SEMICOLON)(advanceLexer)
      *> whenTokenIs(_ == IMPORT)(loop)
    )

    whenTokenIs(_ == IMPORT)(markStart(
         loop
      *> markDone(???/*IMPORTS*/)
    ))
  }

  val pUnknown: P0 =
    pwhenM(eof)(markStart(
      consumeUntilEOF
      *> markDone(???/*UNKNOWN*/)
    ))

  val top: P0 = (
    pModule
    *> pImports
    *> pUnknown
  )
}
