

package Print.preview;

import common.Constants;
import Print.*;
import Force.*;
import Force.Scenario;
import battleforce.BattleForce;
import dialog.dlgImageMgr;
import filehandlers.ImageTracker;
import filehandlers.MechWriter;
import filehandlers.Media;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.Vector;
import java.util.prefs.Preferences;

public class dlgPreview extends javax.swing.JFrame implements ActionListener {
    private final static double DEFAULT_ZOOM_FACTOR_STEP = .5;
    protected Pageable pageable;
    private Preview preview;
    private Scenario scenario;
    private PagePrinter printer;
    private ImageTracker imageTracker;
    private Media media = new Media();
    private Preferences bfbPrefs = Preferences.userNodeForPackage("/bfb/gui/frmBase".getClass());
    private Preferences sswPrefs = Preferences.userNodeForPackage("/ssw/gui/frmMain".getClass());

    public dlgPreview(String title, Component owner, Pageable pageable, double zoom, ImageTracker images) {
        super(title);
        initComponents();
        imageTracker = images;
        preview = new Preview(pageable, zoom, spnPreview.getSize());
        spnPreview.setViewportView(preview);

        btnZoomIn.setAction(new ZoomAction("Zoom In", "magnifier-zoom.png", preview, DEFAULT_ZOOM_FACTOR_STEP, false));
        btnZoomOut.setAction(new ZoomAction("Zoom Out", "magnifier-zoom-out.png", preview, -DEFAULT_ZOOM_FACTOR_STEP, false));

        btnBack.setAction(new BrowseAction("Prev", "arrow-180.png", preview, -1));
        btnForward.setAction(new BrowseAction("Next", "arrow.png", preview, 1));

        btnPageWidth.setAction(new ZoomAction("Width", "document-resize.png", preview, preview.getWidthZoom(), true));
        btnPageHeight.setAction(new ZoomAction("Page", "document-resize-actual.png", preview, preview.getHeightZoom(), true));

        spnPreview.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                preview.setViewportSize( e.getComponent().getSize() );
                btnPageWidth.setAction(new ZoomAction("Width", "document-resize.png", preview, preview.getWidthZoom(), true));
                btnPageHeight.setAction(new ZoomAction("Page", "document-resize-actual.png", preview, preview.getHeightZoom(), true));
            }
        });
    }

    public dlgPreview(String title, Component owner, Pageable pageable, ImageTracker images) {
        this(title, owner, pageable, 0.0, images);
    }

    public dlgPreview(String title, Component owner, PagePrinter printer, Scenario scenario, ImageTracker images) {
        this(title, owner, printer.Preview(), 0.0, images);
        setScenario(scenario);
        setPrinter(printer);
    }

    public dlgPreview(String title, Component owner, Printable printable, PageFormat format, int pages, double zoom, ImageTracker images) {
        this(title, owner, new MyPageable(printable, format, pages), zoom, images);
    }

    public dlgPreview(String title, Component owner, Printable printable, PageFormat format, int pages, ImageTracker images) {
        this(title, owner, printable, format, pages, 0.0, images);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    public void setScenario( Scenario scenario ) {
        this.scenario = scenario;
        scenario.getAttackerForce().sortForPrinting();
        scenario.getDefenderForce().sortForPrinting();
    }

    public void setPrinter(PagePrinter printer) {
        this.printer = printer;

        chkCanon.setSelected(sswPrefs.getBoolean(Constants.Format_CanonPattern, false));
        chkTables.setSelected(sswPrefs.getBoolean(Constants.Format_Tables, false));
        chkUseHexConversion.setSelected(sswPrefs.getBoolean(Constants.Format_ConvertTerrain, false));
        cmbHexConvFactor.setSelectedItem(sswPrefs.getInt(Constants.Format_TerrainModifier, 1));

        chkPrintForce.setSelected(bfbPrefs.getBoolean(Constants.Print_ForceList, true));
        chkPrintFireChits.setSelected(bfbPrefs.getBoolean(Constants.Print_FireDeclaration, false));
        chkPrintScenario.setSelected(bfbPrefs.getBoolean(Constants.Print_Scenario, false));
        chkPrintRecordsheets.setSelected(bfbPrefs.getBoolean(Constants.Print_Recordsheet, true));
        chkPrintBattleforce.setSelected(bfbPrefs.getBoolean(Constants.Print_BattleForce, false));

        chkBFOnePerPage.setSelected(bfbPrefs.getBoolean(Constants.Format_OneForcePerPage, false));
        chkBFBacks.setSelected(bfbPrefs.getBoolean("BF_Backs", false));
        chkUseColor.setSelected(bfbPrefs.getBoolean("BF_Color", false));

        cmbBFSheetType.setSelectedIndex(bfbPrefs.getInt(Constants.Format_BattleForceSheetChoice, 0));
        cmbRSType.setSelectedIndex(bfbPrefs.getInt(Constants.Format_RecordsheetChoice, 0));

        Verify();
    }

    private static class MyPageable implements Pageable {
        public MyPageable(Printable printable, PageFormat format, int pages) {
            this.printable = printable;
            this.format = format;
            this.pages = pages;
        }

        public int getNumberOfPages() {
            return pages;
        }

        public Printable getPrintable(int index) {
            if (index >= pages) throw new IndexOutOfBoundsException();
            return printable;
        }

        public PageFormat getPageFormat(int index) {
            if (index >= pages) throw new IndexOutOfBoundsException();
            return format;
        }

        private Printable printable;
        private PageFormat format;
        private int pages;
    }

    private void refresh() {
        preview.setPageable(printer.Preview());
        preview.repaint();
    }

    private void Verify() {
        cmbRSType.setEnabled(chkPrintRecordsheets.isSelected());
        chkTables.setEnabled(chkPrintRecordsheets.isSelected());
        chkCanon.setEnabled(chkPrintRecordsheets.isSelected());

        chkImage.setEnabled(chkPrintRecordsheets.isSelected() || chkPrintBattleforce.isSelected());
        chkLogo.setEnabled(chkPrintRecordsheets.isSelected() || chkPrintBattleforce.isSelected() || chkPrintForce.isSelected());
        chkPrintGroup.setEnabled(chkPrintRecordsheets.isSelected() || chkPrintBattleforce.isSelected());

        cmbBFSheetType.setEnabled(chkPrintBattleforce.isSelected());
        chkBFOnePerPage.setEnabled(chkPrintBattleforce.isSelected());
        chkBFTerrainMV.setEnabled(chkPrintBattleforce.isSelected());
        chkUseColor.setEnabled(chkPrintBattleforce.isSelected());
        chkBFBacks.setEnabled(chkPrintBattleforce.isSelected());

        chkUseHexConversion.setEnabled(chkPrintRecordsheets.isSelected());
        cmbHexConvFactor.setEnabled(chkPrintRecordsheets.isSelected());

        if ( cmbRSType.getSelectedIndex() == 1 ) {
            chkTables.setSelected(false);
            chkTables.setEnabled(false);
            chkCanon.setSelected(true);
            chkCanon.setEnabled(false);

            chkPrintGroup.setSelected(false);
            chkPrintGroup.setEnabled(false);

            chkUseHexConversion.setSelected(false);
            chkUseHexConversion.setEnabled(false);
            cmbHexConvFactor.setSelectedItem(1);
            cmbHexConvFactor.setEnabled(false);
        }

        if ( chkPrintBattleforce.isSelected() ) {
            switch ( cmbBFSheetType.getSelectedIndex() ) {
                case 0:
                    chkBFOnePerPage.setEnabled(true);
                    chkBFBacks.setEnabled(false);
                    chkUseColor.setEnabled(false);
                    break;
                case 1:
                    chkBFOnePerPage.setEnabled(false);
                    chkBFBacks.setEnabled(true);
                    chkUseColor.setEnabled(true);
                    break;
                case 2:
                    chkBFOnePerPage.setSelected(true);
                    chkBFOnePerPage.setEnabled(false);
                    chkBFBacks.setEnabled(false);
                    chkUseColor.setEnabled(false);
                    break;
                default:
                    chkBFOnePerPage.setEnabled(false);
                    chkBFBacks.setEnabled(true);
                    chkUseColor.setEnabled(true);
            }
        }
    }

    private void PrinterSetup() {
        WaitCursor();
        printer.Clear();

        if ( chkImage.isSelected() ) {
            if ( sswPrefs.get("DefaultImagePath", "").isEmpty() ) {
                if ( Media.Options(this, "You must set a default image path for images to be found, would you like to do that now?", "Image Lookup Warning") == Media.OK ) {
                    String dir = media.GetDirectorySelection(this);
                    sswPrefs.put("DefaultImagePath", dir);
                }
            }
        }
        
        if ( chkPrintForce.isSelected() ) {
            ForceListPrinter sheet = new ForceListPrinter(imageTracker);
            sheet.setPrintLogo(chkLogo.isSelected());
            sheet.setTitle(scenario.getName());
            sheet.AddForces(scenario.getForces());
            printer.Append( BFBPrinter.Letter.toPage(), sheet );
        }

        if ( chkPrintScenario.isSelected() ) {
            ScenarioPrinter sheet = new ScenarioPrinter(scenario, imageTracker);
            printer.Append( BFBPrinter.Letter.toPage(), sheet);
        }

        if ( chkPrintFireChits.isSelected() ) {
            PrintDeclaration fire = new PrintDeclaration(imageTracker);
            fire.AddForces(scenario.getForces());
            printer.Append( BFBPrinter.Letter.toPage(), fire );
        }

        if ( chkPrintBattleforce.isSelected() ) {
            int MaxUnits = 8;
            imageTracker.preLoadBattleForceImages();
            switch ( cmbBFSheetType.getSelectedIndex() ) {
                case 0:
                    if ( chkBFOnePerPage.isSelected() ) {
                        Vector<BattleForce> forcelist = new Vector<BattleForce>();
                        forcelist.addAll(scenario.getAttackerForce().toBattleForceByGroup( 12 ));
                        if ( scenario.getDefenderForce().getUnits().size() > 0 ) { forcelist.addAll(scenario.getDefenderForce().toBattleForceByGroup( 12 )); }

                        for ( BattleForce f : forcelist ) {
                            BattleforcePrinter bf = new BattleforcePrinter(f, imageTracker);
                            bf.setPrintLogo(chkLogo.isSelected());
                            bf.setPrintMechs(chkImage.isSelected());
                            if ( chkBFTerrainMV.isSelected() ) bf.setTerrain(true);
                            printer.Append( BFBPrinter.Letter.toPage(), bf);
                        }
                    } else {
                        BattleforcePrinter topBF = new BattleforcePrinter(scenario.getAttackerForce().toBattleForce(), imageTracker);
                        topBF.setPrintLogo(chkLogo.isSelected());
                        topBF.setPrintMechs(chkImage.isSelected());
                        if ( chkBFTerrainMV.isSelected() ) topBF.setTerrain(true);
                        printer.Append( BFBPrinter.Letter.toPage(), topBF );

                        if ( scenario.getDefenderForce().getUnits().size() > 0 ) {
                            BattleforcePrinter bottomBF = new BattleforcePrinter(scenario.getDefenderForce().toBattleForce(), imageTracker);
                            bottomBF.setPrintLogo(chkLogo.isSelected());
                            bottomBF.setPrintMechs(chkImage.isSelected());
                            if ( chkBFTerrainMV.isSelected() ) bottomBF.setTerrain(true);
                            printer.Append( BFBPrinter.Letter.toPage(), bottomBF );
                        }
                    }
                    break;

                case 1:
                    if ( chkBFBacks.isSelected() ) MaxUnits = 4;
                    for ( BattleForce f : scenario.toBattleForceBySize(MaxUnits) ) {
                        QSHorizontalCardPrinter hqs = new QSHorizontalCardPrinter(f, imageTracker);
                        if ( !chkUseColor.isSelected() ) hqs.setBlackAndWhite();
                        hqs.setPrintLogo(chkLogo.isSelected());
                        hqs.setPrintMechs(chkImage.isSelected());
                        hqs.setPrintWarriorData(chkPrintGroup.isSelected());
                        if ( chkBFTerrainMV.isSelected() ) hqs.setTerrain(true);
                        printer.Append( BFBPrinter.FullLetter.toPage(), hqs);
                    }
                    break;

                case 2:
                    for ( BattleForce f : scenario.toBattleForceByGroup( 6 ) ) {
                        BattleforceCardPrinter bf = new BattleforceCardPrinter(f, imageTracker);
                        bf.setPrintLogo(chkLogo.isSelected());
                        bf.setPrintMechs(chkImage.isSelected());
                        bf.setPrintWarriorData(chkPrintGroup.isSelected());
                        if ( chkBFTerrainMV.isSelected() ) bf.setTerrain(true);
                        printer.Append( BFBPrinter.Letter.toPage(), bf);
                    }
                    break;

                case 3:
                    if ( chkBFBacks.isSelected() ) MaxUnits = 4;
                    for ( BattleForce f : scenario.toBattleForceBySize(MaxUnits) ) {
                        QSVerticalCardPrinter qs = new QSVerticalCardPrinter(f, imageTracker);
                        if ( !chkUseColor.isSelected() ) qs.setBlackAndWhite();
                        qs.setCardBack(chkBFBacks.isSelected());
                        qs.setPrintLogo(chkLogo.isSelected());
                        qs.setPrintMechs(chkImage.isSelected());
                        qs.setPrintWarriorData(chkPrintGroup.isSelected());
                        if ( chkBFTerrainMV.isSelected() ) qs.setTerrain(true);
                        PageFormat letter = BFBPrinter.FullLetter.toPage();
                        letter.setOrientation(PageFormat.LANDSCAPE);
                        printer.Append( letter, qs);
                    }
                    break;

                default:
            }
        }

        if ( chkPrintRecordsheets.isSelected() ) {
            imageTracker.preLoadMechImages();
            for ( Force force : scenario.getForces() ) {
                for ( Group g : force.Groups ) {
                    for ( Unit u : g.getUnits() ) {
                        u.LoadMech();
                        PrintMech pm = new PrintMech(u.m,u.getMechwarrior(), u.getGunnery(), u.getPiloting(), imageTracker);
                        pm.setCanon(chkCanon.isSelected());
                        pm.setCharts(chkTables.isSelected());
                        pm.setPrintMech(chkImage.isSelected());
                        pm.setPrintLogo(chkLogo.isSelected());
                        if ( chkPrintGroup.isSelected() ) pm.setGroupName( g.getName() + " [" + g.getForce().ForceName + "]" );
                        if ( chkUseHexConversion.isSelected() ) pm.SetMiniConversion(cmbHexConvFactor.getSelectedIndex());
                        if ( chkLogo.isSelected() ) pm.setLogoImage(imageTracker.getImage(g.getLogo()));
                        if ( cmbRSType.getSelectedIndex() == 1 ) pm.setTRO(true);
                        printer.Append( BFBPrinter.Letter.toPage(), pm);
                    }
                }
            }
        }
        refresh();
        DefaultCursor();
    }

    private void setPreferences() {
        sswPrefs.putBoolean(Constants.Format_CanonPattern, chkCanon.isSelected());
        sswPrefs.putBoolean(Constants.Format_Tables, chkTables.isSelected());
        sswPrefs.putBoolean(Constants.Format_ConvertTerrain, chkUseHexConversion.isSelected());
        sswPrefs.putInt(Constants.Format_TerrainModifier, cmbHexConvFactor.getSelectedIndex());

        bfbPrefs.putBoolean(Constants.Print_ForceList, chkPrintForce.isSelected());
        bfbPrefs.putBoolean(Constants.Print_FireDeclaration, chkPrintFireChits.isSelected());
        bfbPrefs.putBoolean(Constants.Print_Scenario, chkPrintScenario.isSelected());
        bfbPrefs.putBoolean(Constants.Print_Recordsheet, chkPrintRecordsheets.isSelected());
        bfbPrefs.putBoolean(Constants.Print_BattleForce, chkPrintBattleforce.isSelected());
        bfbPrefs.putBoolean(Constants.Format_OneForcePerPage, chkBFOnePerPage.isSelected());
        bfbPrefs.putBoolean("BF_Backs", chkBFBacks.isSelected());
        bfbPrefs.putBoolean("BF_Color", chkUseColor.isSelected());

        bfbPrefs.putInt(Constants.Format_BattleForceSheetChoice, cmbBFSheetType.getSelectedIndex());
        bfbPrefs.putInt(Constants.Format_RecordsheetChoice, cmbRSType.getSelectedIndex());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spnPreview = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        pnlPrintOptions = new javax.swing.JPanel();
        chkPrintForce = new javax.swing.JCheckBox();
        chkPrintFireChits = new javax.swing.JCheckBox();
        chkPrintRecordsheets = new javax.swing.JCheckBox();
        chkPrintBattleforce = new javax.swing.JCheckBox();
        chkPrintScenario = new javax.swing.JCheckBox();
        pnlHow = new javax.swing.JPanel();
        chkTables = new javax.swing.JCheckBox();
        chkCanon = new javax.swing.JCheckBox();
        chkUseHexConversion = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        cmbHexConvFactor = new javax.swing.JComboBox();
        lblInches = new javax.swing.JLabel();
        lblOneHex = new javax.swing.JLabel();
        cmbRSType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        chkLogo = new javax.swing.JCheckBox();
        chkImage = new javax.swing.JCheckBox();
        chkPrintGroup = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        chkBFOnePerPage = new javax.swing.JCheckBox();
        cmbBFSheetType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        chkBFTerrainMV = new javax.swing.JCheckBox();
        chkUseColor = new javax.swing.JCheckBox();
        chkBFBacks = new javax.swing.JCheckBox();
        jToolBar1 = new javax.swing.JToolBar();
        btnPreview = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnBack = new javax.swing.JButton();
        btnForward = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPageWidth = new javax.swing.JButton();
        btnPageHeight = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnCheckImages = new javax.swing.JButton();
        btnClearImages = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnSaveOptions = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnCloseDialog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pnlPrintOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("What to Print"));

        chkPrintForce.setSelected(true);
        chkPrintForce.setText("Force List");
        chkPrintForce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintForceActionPerformed(evt);
            }
        });

        chkPrintFireChits.setText("Fire Declaration Chits");
        chkPrintFireChits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkPrintRecordsheets.setText("Unit Recordsheets");
        chkPrintRecordsheets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkPrintBattleforce.setText("BattleForce Sheets");
        chkPrintBattleforce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkPrintScenario.setText("Scenario Sheet");
        chkPrintScenario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlPrintOptionsLayout = new javax.swing.GroupLayout(pnlPrintOptions);
        pnlPrintOptions.setLayout(pnlPrintOptionsLayout);
        pnlPrintOptionsLayout.setHorizontalGroup(
            pnlPrintOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrintOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrintOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkPrintForce)
                    .addComponent(chkPrintFireChits)
                    .addComponent(chkPrintBattleforce)
                    .addComponent(chkPrintRecordsheets)
                    .addComponent(chkPrintScenario))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        pnlPrintOptionsLayout.setVerticalGroup(
            pnlPrintOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrintOptionsLayout.createSequentialGroup()
                .addComponent(chkPrintForce)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPrintFireChits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPrintScenario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPrintBattleforce)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPrintRecordsheets)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlHow.setBorder(javax.swing.BorderFactory.createTitledBorder("Recordsheet Options"));

        chkTables.setSelected(true);
        chkTables.setText("Print Charts and Tables");
        chkTables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkCanon.setSelected(true);
        chkCanon.setText("Print Canon Dot Patterns");
        chkCanon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkUseHexConversion.setText("Use Miniatures Scale");
        chkUseHexConversion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUseHexConversionActionPerformed(evt);
            }
        });

        cmbHexConvFactor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
        cmbHexConvFactor.setEnabled(false);
        cmbHexConvFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbHexConvFactorActionPerformed(evt);
            }
        });

        lblInches.setText("Inches");
        lblInches.setEnabled(false);

        lblOneHex.setText("One Hex equals");
        lblOneHex.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(cmbHexConvFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblInches))
                    .addComponent(lblOneHex))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(lblOneHex)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbHexConvFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblInches)))
                .addGap(1, 1, 1))
        );

        cmbRSType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Total Warfare", "Recordsheet", "Tactical Operations" }));
        cmbRSType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        jLabel4.setText("Sheet Type:");

        javax.swing.GroupLayout pnlHowLayout = new javax.swing.GroupLayout(pnlHow);
        pnlHow.setLayout(pnlHowLayout);
        pnlHowLayout.setHorizontalGroup(
            pnlHowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbRSType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlHowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHowLayout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkTables)
                    .addComponent(chkUseHexConversion)
                    .addComponent(chkCanon)))
            .addComponent(jLabel4)
        );
        pnlHowLayout.setVerticalGroup(
            pnlHowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHowLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbRSType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkTables)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkCanon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkUseHexConversion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("General Options"));

        chkLogo.setSelected(true);
        chkLogo.setText("Print Unit Logo");
        chkLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkImage.setSelected(true);
        chkImage.setText("Print Mech Images ");
        chkImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        chkPrintGroup.setSelected(true);
        chkPrintGroup.setText("Print Warrior/Unit Info");
        chkPrintGroup.setToolTipText("Includes Pilot Name, Skill, Unit, Group");
        chkPrintGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrintGroupitemChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkLogo)
                    .addComponent(chkImage)
                    .addComponent(chkPrintGroup))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(chkImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPrintGroup))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("BattleForce Options"));

        chkBFOnePerPage.setText("One Unit Per Page");
        chkBFOnePerPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBFOnePerPageActionPerformed(evt);
            }
        });

        cmbBFSheetType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "*Strategic Ops", "*QuickStrike Cards", "BattleForce Sheet", "Vertical Cards" }));
        cmbBFSheetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemChanged(evt);
            }
        });

        jLabel3.setText("Sheet Type:");

        chkBFTerrainMV.setText("Use Miniatures Scale");
        chkBFTerrainMV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBFTerrainMVActionPerformed(evt);
            }
        });

        chkUseColor.setText("Print Full Color");
        chkUseColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUseColorActionPerformed(evt);
            }
        });

        chkBFBacks.setText("Print Card Backs");
        chkBFBacks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBFBacksActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkBFTerrainMV))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkBFOnePerPage))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmbBFSheetType, 0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkUseColor))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkBFBacks)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbBFSheetType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(chkBFOnePerPage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBFTerrainMV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkUseColor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBFBacks))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlHow, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPrintOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlPrintOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(pnlHow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrow-circle-double.png"))); // NOI18N
        btnPreview.setText("Refresh");
        btnPreview.setToolTipText("Refresh Print Preview");
        btnPreview.setFocusable(false);
        btnPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPreview);
        jToolBar1.add(jSeparator5);

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrow-180.png"))); // NOI18N
        btnBack.setText("Prev");
        btnBack.setFocusable(false);
        btnBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnBack);

        btnForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/arrow.png"))); // NOI18N
        btnForward.setText("Next");
        btnForward.setFocusable(false);
        btnForward.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnForward.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnForward);
        jToolBar1.add(jSeparator1);

        btnPageWidth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/document-resize.png"))); // NOI18N
        btnPageWidth.setText("Width");
        btnPageWidth.setFocusable(false);
        btnPageWidth.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPageWidth.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnPageWidth);

        btnPageHeight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/document-resize-actual.png"))); // NOI18N
        btnPageHeight.setText("Page");
        btnPageHeight.setFocusable(false);
        btnPageHeight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPageHeight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnPageHeight);
        jToolBar1.add(jSeparator3);

        btnZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier-zoom.png"))); // NOI18N
        btnZoomIn.setText("Zoom In");
        btnZoomIn.setFocusable(false);
        btnZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnZoomIn);

        btnZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/magnifier-zoom-out.png"))); // NOI18N
        btnZoomOut.setText("Zoom Out");
        btnZoomOut.setFocusable(false);
        btnZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnZoomOut);
        jToolBar1.add(jSeparator4);

        btnCheckImages.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/images-stack.png"))); // NOI18N
        btnCheckImages.setText("Images");
        btnCheckImages.setFocusable(false);
        btnCheckImages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCheckImages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCheckImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckImagesActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCheckImages);

        btnClearImages.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/eraser.png"))); // NOI18N
        btnClearImages.setText("Clear All");
        btnClearImages.setFocusable(false);
        btnClearImages.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClearImages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClearImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearImagesActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClearImages);
        jToolBar1.add(jSeparator2);

        btnSaveOptions.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/disk-black.png"))); // NOI18N
        btnSaveOptions.setText("Defaults");
        btnSaveOptions.setToolTipText("Save Default Options");
        btnSaveOptions.setFocusable(false);
        btnSaveOptions.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveOptions.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveOptionsActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveOptions);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer.png"))); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setFocusable(false);
        btnPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrint);

        btnCloseDialog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/home.png"))); // NOI18N
        btnCloseDialog.setText("Close");
        btnCloseDialog.setFocusable(false);
        btnCloseDialog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCloseDialog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCloseDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseDialogActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCloseDialog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spnPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseDialogActionPerformed
        dispose();
}//GEN-LAST:event_btnCloseDialogActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        setPreferences();
        printer.Print();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void chkUseHexConversionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUseHexConversionActionPerformed
        lblOneHex.setEnabled( chkUseHexConversion.isSelected() );
        cmbHexConvFactor.setEnabled( chkUseHexConversion.isSelected() );
        lblInches.setEnabled( chkUseHexConversion.isSelected() );

        Verify();
}//GEN-LAST:event_chkUseHexConversionActionPerformed

    private void chkBFOnePerPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBFOnePerPageActionPerformed
        Verify();
}//GEN-LAST:event_chkBFOnePerPageActionPerformed

    private void cmbHexConvFactorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbHexConvFactorActionPerformed
        Verify();
    }//GEN-LAST:event_cmbHexConvFactorActionPerformed

    private void itemChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemChanged
        Verify();
}//GEN-LAST:event_itemChanged

    private void btnCheckImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckImagesActionPerformed
        dlgImageMgr dlgImg = new dlgImageMgr(null, scenario.getForces(), imageTracker);
        if ( dlgImg.hasWork ) {
            dlgImg.setLocationRelativeTo(this);
            dlgImg.setVisible(true);
        } else {
            Media.Messager("All units have images selected.");
        }
    }//GEN-LAST:event_btnCheckImagesActionPerformed

    private void chkPrintForceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintForceActionPerformed
        Verify();
    }//GEN-LAST:event_chkPrintForceActionPerformed

    private void chkBFTerrainMVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBFTerrainMVActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_chkBFTerrainMVActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        btnCloseDialogActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void chkUseColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUseColorActionPerformed
        Verify();
}//GEN-LAST:event_chkUseColorActionPerformed

    private void chkBFBacksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBFBacksActionPerformed
        Verify();
}//GEN-LAST:event_chkBFBacksActionPerformed

    private void btnClearImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearImagesActionPerformed
        MechWriter writer = new MechWriter();

        if ( Media.Options(this, "Are you sure you want to remove all pre-selected images from the units in this list?", "Confirm Image Removal") == Media.OK) {
            WaitCursor();
            for ( Group g : scenario.getGroups() ) {
                for ( Unit u : g.getUnits() ) {
                    try {
                        u.LoadMech();
                        u.m.SetSSWImage("../Images/No_Image.png");
                        writer.setMech(u.m);
                        writer.WriteXML(u.Filename);
                    } catch ( Exception e ) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            DefaultCursor();
            Media.Messager("Images have been removed from all units in the list.");
        }
    }//GEN-LAST:event_btnClearImagesActionPerformed

    private void chkPrintGroupitemChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrintGroupitemChanged
        // TODO add your handling code here:
}//GEN-LAST:event_chkPrintGroupitemChanged

    private void btnSaveOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveOptionsActionPerformed
        setPreferences();
}//GEN-LAST:event_btnSaveOptionsActionPerformed

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        PrinterSetup();
}//GEN-LAST:event_btnPreviewActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        PrinterSetup();
    }//GEN-LAST:event_formWindowOpened

    private void WaitCursor() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    private void DefaultCursor() {
        setCursor(Cursor.getDefaultCursor());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCheckImages;
    private javax.swing.JButton btnClearImages;
    private javax.swing.JButton btnCloseDialog;
    private javax.swing.JButton btnForward;
    private javax.swing.JButton btnPageHeight;
    private javax.swing.JButton btnPageWidth;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSaveOptions;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JCheckBox chkBFBacks;
    private javax.swing.JCheckBox chkBFOnePerPage;
    private javax.swing.JCheckBox chkBFTerrainMV;
    private javax.swing.JCheckBox chkCanon;
    private javax.swing.JCheckBox chkImage;
    private javax.swing.JCheckBox chkLogo;
    private javax.swing.JCheckBox chkPrintBattleforce;
    private javax.swing.JCheckBox chkPrintFireChits;
    private javax.swing.JCheckBox chkPrintForce;
    private javax.swing.JCheckBox chkPrintGroup;
    private javax.swing.JCheckBox chkPrintRecordsheets;
    private javax.swing.JCheckBox chkPrintScenario;
    private javax.swing.JCheckBox chkTables;
    private javax.swing.JCheckBox chkUseColor;
    private javax.swing.JCheckBox chkUseHexConversion;
    private javax.swing.JComboBox cmbBFSheetType;
    private javax.swing.JComboBox cmbHexConvFactor;
    private javax.swing.JComboBox cmbRSType;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblInches;
    private javax.swing.JLabel lblOneHex;
    private javax.swing.JPanel pnlHow;
    private javax.swing.JPanel pnlPrintOptions;
    private javax.swing.JScrollPane spnPreview;
    // End of variables declaration//GEN-END:variables
}
