package edu.ualberta.med.scannerconfig.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class InputDialog extends Dialog {
    String value;

    String title;
    String message;

    private static final I18n i18n = I18nFactory.getI18n(InputDialog.class);

    public InputDialog(Shell parent, int style, String title, String message) {
        super(parent, style);

        this.title = title;
        this.message = message;

    }

    @SuppressWarnings("nls")
    public String open() {
        Shell parent = getParent();
        final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER
            | SWT.APPLICATION_MODAL);
        shell.setText(this.title);

        shell.setLayout(new GridLayout(4, false));

        Label label = new Label(shell, SWT.NULL);
        label.setText(this.message);

        final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);

        final Button buttonOK = new Button(shell, SWT.PUSH);
        buttonOK.setText(i18n.tr("OK"));
        buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        Button buttonCancel = new Button(shell, SWT.PUSH);
        buttonCancel.setText(i18n.tr("Cancel"));

        text.addListener(SWT.Modify, new Listener() {
            @Override
            public void handleEvent(Event event) {
                value = text.getText();
                buttonOK.setEnabled((value != null));
            }
        });
        text.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    shell.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        buttonOK.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                shell.dispose();
            }
        });

        buttonCancel.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                value = null;
                shell.dispose();
            }
        });
        shell.addListener(SWT.Traverse, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.TRAVERSE_ESCAPE)
                    event.doit = false;
            }
        });

        text.setText("");
        shell.pack();
        shell.open();

        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        return value;
    }

    @SuppressWarnings("nls")
    public static void main(String[] args) {
        Shell shell = new Shell();
        InputDialog dialog = new InputDialog(
            shell,
            SWT.NONE,
            "Bacon",
            "Enter fish:");
        System.out.println(dialog.open());
    }
}
