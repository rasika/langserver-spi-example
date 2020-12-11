package org.example.command;

import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.commons.ExecuteCommandContext;
import org.ballerinalang.langserver.commons.command.LSCommandExecutorException;
import org.ballerinalang.langserver.commons.command.spi.LSCommandExecutor;
import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentEdit;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Character.LINE_SEPARATOR;

;

@JavaSPIService("org.ballerinalang.langserver.commons.codeaction.spi.LSCodeActionProvider")
public class CustomCommandExecutor implements LSCommandExecutor {
    public static final String CMD_NAME = "ballerina.executor.custom";

    @Override
    public Object execute(ExecuteCommandContext context) throws LSCommandExecutorException {
        // Create text-edit
        List<TextEdit> textEdits = new ArrayList<>();
        String editText = LINE_SEPARATOR + "test" + LINE_SEPARATOR;
        Position editPos = new Position(0, 0);
        textEdits.add(new TextEdit(new Range(editPos, editPos), editText));
        // Create workspace edit
        VersionedTextDocumentIdentifier textDocumentIdentifier = new VersionedTextDocumentIdentifier();
        TextDocumentEdit textDocumentEdit = new TextDocumentEdit(textDocumentIdentifier, textEdits);
        WorkspaceEdit workspaceEdit = new WorkspaceEdit(Collections.singletonList(Either.forLeft(textDocumentEdit)));
        ApplyWorkspaceEditParams applyWorkspaceEditParams = new ApplyWorkspaceEditParams(workspaceEdit);
        // Publish edit through client
        LanguageClient languageClient = context.getLanguageClient();
        if (languageClient != null) {
            languageClient.applyEdit(applyWorkspaceEditParams);
        }
        return applyWorkspaceEditParams;
    }

    @Override
    public String getCommand() {
        return CMD_NAME;
    }
}
