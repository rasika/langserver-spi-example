package org.example.codeaction;

import io.ballerina.compiler.api.symbols.Symbol;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.compiler.syntax.tree.NonTerminalNode;
import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.codeaction.providers.AbstractCodeActionProvider;
import org.ballerinalang.langserver.commons.CodeActionContext;
import org.ballerinalang.langserver.commons.codeaction.CodeActionNodeType;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.ballerinalang.langserver.common.utils.CommonUtil.LINE_SEPARATOR;

@JavaSPIService("org.ballerinalang.langserver.commons.codeaction.spi.LSCodeActionProvider")
public class CustomNodeBasedCodeAction extends AbstractCodeActionProvider {
    private static final String CODE_ACTION_TITLE = "test-node-code-action";

    public CustomNodeBasedCodeAction() {
        // Attach this code-action only for functions
        super(Collections.singletonList(CodeActionNodeType.FUNCTION));
    }

    @Override
    public List<CodeAction> getNodeBasedCodeActions(CodeActionContext context) {
        // Get code-action position details
        NonTerminalNode matchedNode = context.positionDetails().matchedNode();
        Symbol matchedSymbol = context.positionDetails().matchedSymbol();
        TypeSymbol matchedExprType = context.positionDetails().matchedExprType();

        List<TextEdit> edits = new ArrayList<>();
        String editText = LINE_SEPARATOR + matchedNode.kind() + LINE_SEPARATOR;
        Position editPos = context.cursorPosition();
        edits.add(new TextEdit(new Range(editPos, editPos), editText));
        return Collections.singletonList(createQuickFixCodeAction(CODE_ACTION_TITLE, edits, context.fileUri()));
    }
}
