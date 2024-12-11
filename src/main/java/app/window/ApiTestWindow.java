package app.window;

import app.dto.AuditLogDTO;
import app.enums.ApiTest;
import app.model.AuditLogTableModel;
import app.service.AuditLogService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ApiTestWindow {

    private static final float FONT_SIZE = 16f;

    private static final String BASE_URL = "http://localhost:8080";

    @Getter
    private JFrame jFrame;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private JTextArea apiResultTextArea;

    private AuditLogTableModel model;

    private final RestTemplate restTemplate;

    private final AuditLogService service;

    public ApiTestWindow(RestTemplate restTemplate, AuditLogService service) {
        this.restTemplate = restTemplate;
        this.service = service;
        this.setupJFrame();
        this.createTab1();
        this.createTab2();

        this.setFont(tabbedPane);

        final Container container = this.jFrame.getContentPane();
        container.setLayout(new FlowLayout(FlowLayout.LEFT));
        container.add(tabbedPane);

        this.jFrame.pack();
    }

    private void setupJFrame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        this.jFrame = new JFrame("UserOperationRecordingApplication");
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setSize(dimension.width / 2, dimension.height / 2);
        this.jFrame.setResizable(false);

        final int x = (int) ((dimension.getWidth() - this.jFrame.getWidth()) / 2);
        final int y = (int) ((dimension.getHeight() - this.jFrame.getHeight()) / 2);
        this.jFrame.setLocation(x, y);
    }

    private void createTab1() {
        final java.util.List<JPanel> panels = new ArrayList<>();
        this.createButtonBlock(panels);
        this.createApiResultBlock(panels);

        final JPanel jp = new JPanel(new GridLayout(panels.size(), 1));
        for (final JPanel panel : panels) {
            jp.add(panel);
        }

        final JScrollPane jScrollPane = new JScrollPane(jp);
        tabbedPane.add("發送請求", jScrollPane);
    }

    private void createButtonBlock(final List<JPanel> panels) {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        Arrays.stream(ApiTest.values()).forEach(apiTest -> {
            final JButton button = new JButton(apiTest.getButtonName());
            button.addActionListener(ae -> {
                apiResultTextArea.setText("");
                try {
                    ResponseEntity<String> entity = null;
                    switch (apiTest.getMethod()) {
                        case GET:
                            entity = restTemplate.getForEntity(BASE_URL + apiTest.getUrl(), String.class);
                            break;
                        case POST:
                            entity = restTemplate.postForEntity(BASE_URL + apiTest.getUrl(), apiTest.getParams(), String.class);
                            break;
                    }
                    apiResultTextArea.setText("Request: " + BASE_URL + apiTest.getUrl() + "\n" + "Response: " + entity.getBody());

                } catch (final Exception e) {
                    log.error(e.getMessage(), e);
                    apiResultTextArea.setText("Request: " + BASE_URL + apiTest.getUrl() + "\n" + "Response: " + e.getMessage());
                } finally {
                    apiResultTextArea.setCaretPosition(0);
                    JOptionPane.showMessageDialog(ApiTestWindow.this.jFrame, "發送成功");
                    this.reloadAuditLog();
                }
            });
            this.setFont(button);
            panel.add(button);
        });
        panels.add(panel);
    }

    private void reloadAuditLog() {
        final List<AuditLogDTO> all = service.findAll();
        model.setAuditLogDTOList(all);
        model.fireTableDataChanged();
    }

    private void createApiResultBlock(final List<JPanel> panels) {
        this.apiResultTextArea = new JTextArea(10, 40);
        this.setFont(this.apiResultTextArea);
        this.apiResultTextArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(this.apiResultTextArea);

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.setPreferredSize(new Dimension(1000, 200));
        panel.add(scrollPane);
        panels.add(panel);
    }

    private void createTab2() {
        final java.util.List<JPanel> panels = new ArrayList<>();
        this.createAuditLogBlock(panels);

        final JPanel jp = new JPanel(new GridLayout(panels.size(), 1));
        for (final JPanel panel : panels) {
            jp.add(panel);
        }

        final JScrollPane jScrollPane = new JScrollPane(jp);
        tabbedPane.add("稽核紀錄", jScrollPane);
    }

    private void createAuditLogBlock(List<JPanel> panels) {
        this.model = new AuditLogTableModel(service.findAll());
        final JTable table = new JTable(model);

        for (int i = 0; i < AuditLogTableModel.COLUMN_WIDTH.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(AuditLogTableModel.COLUMN_WIDTH[i]);
        }

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(1000, 600));
        panel.add(scrollPane, BorderLayout.CENTER);
        panels.add(panel);
    }

    private void addEnterKeyListener(final JButton button) {
        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick();
                }
            }
        });
    }

    private void setFont(final Component c) {
        c.setFont(c.getFont().deriveFont(FONT_SIZE));
    }
}
