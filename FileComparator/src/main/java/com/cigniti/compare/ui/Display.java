package com.cigniti.compare.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import com.cigniti.compare.CompareResultWithExpectedAndActual;
import com.cigniti.compare.PdfComparator;
import com.cigniti.util.CsvToPDF;
import com.cigniti.util.DoxcToPDF;
import com.cigniti.util.GetFileName;
import com.cigniti.util.ImageToPDF;
import com.cigniti.util.JSONtoPDF;
import com.cigniti.util.TextToPDF;
import com.cigniti.util.XmlToPDF;

public class Display {

    private ViewModel viewModel;

    public void init() {
        viewModel = new ViewModel(new CompareResultWithExpectedAndActual());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final BorderLayout borderLayout = new BorderLayout();
        frame.setLayout(borderLayout);
        frame.setMinimumSize(new Dimension(300, 150));
        final Rectangle screenBounds = getDefaultScreenBounds();
        frame.setSize(Math.min(screenBounds.width, 1700), Math.min(screenBounds.height, 1000));
        frame.setLocation(screenBounds.x, screenBounds.y);
        //System.getProperty("user.dir") + File.separator + "Logos" + File.separator + logo.png;
        Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "Logos" + File.separator + "logo.png");
        frame.setIconImage(icon); 
        //            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        frame.add(toolBar, BorderLayout.PAGE_START);

        ImagePanel leftPanel = new ImagePanel(viewModel.getLeftImage());
        ImagePanel resultPanel = new ImagePanel(viewModel.getDiffImage());

        JScrollPane expectedScrollPane = new JScrollPane(leftPanel);
        expectedScrollPane.setMinimumSize(new Dimension(200, 200));
        JScrollPane actualScrollPane = new JScrollPane(resultPanel);
        actualScrollPane.setMinimumSize(new Dimension(200, 200));
        actualScrollPane.getViewport().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(final ComponentEvent e) {
                resultPanel.setViewSize(e.getComponent().getSize());
                super.componentResized(e);
            }
        });

        expectedScrollPane.getVerticalScrollBar().setModel(actualScrollPane.getVerticalScrollBar().getModel());
        expectedScrollPane.getHorizontalScrollBar().setModel(actualScrollPane.getHorizontalScrollBar().getModel());
        expectedScrollPane.getViewport().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(final ComponentEvent e) {
                leftPanel.setViewSize(e.getComponent().getSize());
                super.componentResized(e);
            }
        });

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, expectedScrollPane, actualScrollPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setOneTouchExpandable(true);
        frame.add(splitPane, BorderLayout.CENTER);

        final JToggleButton expectedButton = new JToggleButton("Expected");

        addToolBarButton(toolBar, "Open files to compare", (event) -> {
            JFileChooser fileChooser = new JFileChooser();
            try {
                if (fileChooser.showDialog(frame, "Please open expected file!") == JFileChooser.APPROVE_OPTION) {
            		try {
						GetFileName.deleteAllFilesFromFolder();
					} catch (Throwable e2) {
						e2.printStackTrace();
					}
                    File expectedFile = fileChooser.getSelectedFile();
                    String expectedFileName = expectedFile.toString();
                    String expectedFileExtension = expectedFileName.substring(expectedFileName.lastIndexOf('.') + 1);
					if (!"pdf".equals(expectedFileExtension)) {
						try {
							expectedFile = new File(getPDFFilePath(expectedFileExtension, expectedFile.toString(), "ExpectedFile"));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}

                    if (fileChooser.showDialog(frame, "Please open actual file!") == JFileChooser.APPROVE_OPTION) {
                        File actualFile = fileChooser.getSelectedFile();
                        String actualFileName = actualFile.toString();
                        String actualFileExtension = actualFileName.substring(actualFileName.lastIndexOf('.') + 1);
						while (!expectedFileExtension.equals(actualFileExtension)) {
							JOptionPane.showMessageDialog(frame, "Please open actual file formate as expected file. i.e '" + expectedFileExtension+ "'");
							if (fileChooser.showDialog(frame, "Open actual file") == JFileChooser.APPROVE_OPTION) {
								actualFile = fileChooser.getSelectedFile();
								actualFileName = actualFile.toString();
								actualFileExtension = actualFileName.substring(actualFileName.lastIndexOf('.') + 1);
							}
						}
						if (!"pdf".equals(actualFileExtension)) {
							try {
								actualFile = new File(getPDFFilePath(actualFileExtension, actualFileName.toString(), "ActualFile"));
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
                        	frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	                        final CompareResultWithExpectedAndActual compareResult = (CompareResultWithExpectedAndActual)
	                                new PdfComparator<>(expectedFile, actualFile,
	                                        new CompareResultWithExpectedAndActual())
	                                        .withExpectedPassword(String.valueOf(askForPassword(expectedFile).getPassword()))
	                                        .withActualPassword(String.valueOf(askForPassword(actualFile).getPassword()))
	                                        .compare();
	                        viewModel = new ViewModel(compareResult);
	                        leftPanel.setImage(viewModel.getLeftImage());
	                        resultPanel.setImage(viewModel.getDiffImage());
	
	                        if (compareResult.isEqual()) {
	                            JOptionPane.showMessageDialog(frame, "The compared documents are identical.");
	                        }
	
	                        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	                        expectedButton.setSelected(true);
                    }
                }
            } catch (IOException ex) {
                DisplayExceptionDialog(frame, ex);
            }
        });

        toolBar.addSeparator();

        addToolBarButton(toolBar, "Previous Page", (event) -> {
            if (viewModel.decreasePage()) {
                leftPanel.setImage(viewModel.getLeftImage());
                resultPanel.setImage(viewModel.getDiffImage());
            }
        });

        addToolBarButton(toolBar, "Next Page", (event) -> {
            if (viewModel.increasePage()) {
                leftPanel.setImage(viewModel.getLeftImage());
                resultPanel.setImage(viewModel.getDiffImage());
            }
        });
        
        toolBar.addSeparator();

        /*final JToggleButton pageZoomButton = new JToggleButton("Zoom Page");
        pageZoomButton.setSelected(true);
        pageZoomButton.addActionListener((event) -> {
            leftPanel.zoomPage();
            resultPanel.zoomPage();
        });*/

        addToolBarButton(toolBar, "Zoom -", (event) -> {
            //pageZoomButton.setSelected(false);
            leftPanel.decreaseZoom();
            resultPanel.decreaseZoom();
        });

        addToolBarButton(toolBar, "Zoom +", (event) -> {
           // pageZoomButton.setSelected(false);
            leftPanel.increaseZoom();
            resultPanel.increaseZoom();
        });

        //toolBar.add(pageZoomButton);

       /* addToolBarButton(toolBar, "Zoom 100%", (event) -> {
            pageZoomButton.setSelected(false);
            leftPanel.zoom100();
            resultPanel.zoom100();
        });*/

        toolBar.addSeparator();

        addToolBarButton(toolBar, "Center Split", (event) -> {
            splitPane.setDividerLocation(0.5);
            splitPane.revalidate();
        });

        toolBar.addSeparator();
        
        addToolBarButton(toolBar, "Reset", (event) -> {
        	BufferedImage image = null;
                leftPanel.setImage(image);
                resultPanel.setImage(image);
        });
        
        toolBar.addSeparator();

        final ButtonGroup buttonGroup = new ButtonGroup();
        expectedButton.setSelected(true);
        expectedButton.addActionListener((event) -> {
            viewModel.showExpected();
            leftPanel.setImage(viewModel.getLeftImage());
        });
        toolBar.add(expectedButton);
        buttonGroup.add(expectedButton);

        final JToggleButton actualButton = new JToggleButton("Actual");
        actualButton.addActionListener((event) -> {
            viewModel.showActual();
            leftPanel.setImage(viewModel.getLeftImage());
        });
        toolBar.add(actualButton);
        buttonGroup.add(actualButton);
        
        frame.setVisible(true);
    }

    private static void DisplayExceptionDialog(final JFrame frame, final IOException ex) {
        final StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        JTextArea textArea = new JTextArea("An unexpected error has occurred: " + ex.getMessage() + "\n\n" + stringWriter);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(900, 700));
        JOptionPane.showMessageDialog(frame, scrollPane);
    }

    private static void addToolBarButton(final JToolBar toolBar, final String label, final ActionListener actionListener) {
        final JButton button = new JButton(label);
        button.addActionListener(actionListener);
        toolBar.add(button);
    }

    private static Rectangle getDefaultScreenBounds() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    }

    private static JPasswordField askForPassword(final File file) throws IOException {
        JPasswordField passwordForFile = new JPasswordField(10);
        if (isInvalidPassword(file, "")) {
            final JLabel label = new JLabel("Enter password: ");
            label.setLabelFor(passwordForFile);

            final JPanel textPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            textPane.add(label);
            textPane.add(passwordForFile);

            JOptionPane.showMessageDialog(
                    null,
                    textPane,
                    "PDF is encrypted",
                    JOptionPane.INFORMATION_MESSAGE);

            label.setText("Password was invalid. Enter password: ");
            while (isInvalidPassword(file, String.valueOf(passwordForFile.getPassword()))) {
                passwordForFile.setText("");
                JOptionPane.showMessageDialog(
                        null,
                        textPane,
                        "PDF is encrypted",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return passwordForFile;
    }

    private static boolean isInvalidPassword(final File file, final String password) throws IOException {
        try {
            PDDocument.load(file, password).close();
        } catch (InvalidPasswordException e) {
            return true;
        }
        return false;
    }
    
    private static String getPDFFilePath(String extension, String filePath, String expectedOrActual) throws Exception{
        String result;
        switch (extension) {
            case "txt":
                result = TextToPDF.textToPDF(filePath, expectedOrActual); 
                break;
            case "docx":
                result = DoxcToPDF.docxToPDF(filePath, expectedOrActual);
                break;
            case "json":
                result = JSONtoPDF.jsonToPDF(filePath, expectedOrActual);
                break;
            case "xml":
                result = XmlToPDF.xmlToPDF(filePath, expectedOrActual);
                break;
            case "jpeg":
                result = ImageToPDF.imageToPDF(filePath, expectedOrActual);
                break;
            case "csv":
                result = CsvToPDF.csvToPDF(filePath, expectedOrActual);
                break;
            default:
                result = "unknown animal";
                break;
        }
        return result;
    }
}
