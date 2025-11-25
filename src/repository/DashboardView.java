/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package repository;

import javax.swing.JDesktopPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kelvincitopkmz
 */
public class DashboardView extends javax.swing.JFrame {

    /**
     * Creates new form DashboardView
     */
    private repository.DashboardViewController controller;
    private JDesktopPane desktop;

    public DashboardView() {
        initComponents();
        controller = new repository.DashboardViewController();
        controller.initialize();
        controller.setDashboardView(this);

        // Cargar datos iniciales en las tablas
        cargarDatosIniciales();
    }

    // En DashboardView.java - AGREGAR ESTE MÉTODO
    public javax.swing.JTable getInventarioTable() {
        return inventarioTable;
    }

    // Método para cargar datos iniciales
    private void cargarDatosIniciales() {
        // Cargar movimientos de inventario
        controller.actualizarTablaInventario();

        // Cargar conteo de inventario (reportes)
        controller.actualizarTablaConteoInventario();

        // Cargar productos en el combo de SKU
        cargarProductosEnCombo();
    }

// Método para cargar productos en el combo
    private void cargarProductosEnCombo() {
        skuInvCombo.removeAllItems();
        skuInvCombo.addItem("Seleccionar...");

        // Obtener productos del repositorio
        repository.ProductoRepositorio productoRepo = new repository.ProductoRepositorio();
        java.util.List<models.Producto> productos = productoRepo.findAll();

        for (models.Producto p : productos) {
            skuInvCombo.addItem(p.getSku());
        }
    }

// Método público para recargar datos (llamar desde otras vistas)
    public void recargarDatos() {
        cargarDatosIniciales();
    }

    public javax.swing.JTable getConteoTable() {
        return conteoTable;
    }

    // Agregar este método para establecer el controller
    public void setController(repository.DashboardViewController controller) {
        this.controller = controller;
    }

    // Método para agregar JInternalFrame al desktop
    public void addInternalFrame(javax.swing.JInternalFrame frame) {
        desktop.add(frame);
        frame.setVisible(true);

        // Centrar el frame en el desktop
        frame.setLocation(
                (desktop.getWidth() - frame.getWidth()) / 2,
                (desktop.getHeight() - frame.getHeight()) / 2
        );

        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            System.err.println("Error al seleccionar frame: " + e.getMessage());
        }

        System.out.println("JInternalFrame agregado al desktop");
    }

    // Método para inicializar la tabla de ventas
    public void initializeVentasTable() {
        // Definir las columnas según tu imagen
        String[] columnNames = {
            "Nro Orden",
            "Fecha",
            "DNI",
            "Nombre",
            "Producto",
            "Cantidad",
            "Precio final",
            "ESTADO"
        };

        // Crear modelo de tabla sin datos inicialmente
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        ventasTable.setModel(model);

        // Ajustar anchos de columnas (opcional)
        ventasTable.getColumnModel().getColumn(0).setPreferredWidth(70);  // Nro Orden
        ventasTable.getColumnModel().getColumn(1).setPreferredWidth(90);  // Fecha
        ventasTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // DNI
        ventasTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Nombre
        ventasTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Producto
        ventasTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Cantidad
        ventasTable.getColumnModel().getColumn(6).setPreferredWidth(90);  // Precio final
        ventasTable.getColumnModel().getColumn(7).setPreferredWidth(100); // ESTADO

        System.out.println("Tabla de ventas inicializada");
    }

// Método para inicializar la tabla de logística
    public void initializeLogisticaTable() {
        // Definir las columnas según la imagen de logística
        String[] columnNames = {
            "Nro Orden",
            "ESTADO",
            "DNI",
            "Nombre",
            "Producto",
            "Cantidad",
            "Prioritario",
            "Departamento",
            "Provincia",
            "Distrito",
            "Dirección",
            "Transportadora",
            "Nro Tracking",
            "Cod Tracking"
        };

        // Crear modelo de tabla sin datos inicialmente
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        logisticaTable.setModel(model);

        // Ajustar anchos de columnas (opcional)
        logisticaTable.getColumnModel().getColumn(0).setPreferredWidth(70);   // Nro Orden
        logisticaTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // ESTADO
        logisticaTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // DNI
        logisticaTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // Nombre
        logisticaTable.getColumnModel().getColumn(4).setPreferredWidth(90);   // Producto
        logisticaTable.getColumnModel().getColumn(5).setPreferredWidth(70);   // Cantidad
        logisticaTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // Prioritario
        logisticaTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // Departamento
        logisticaTable.getColumnModel().getColumn(8).setPreferredWidth(90);   // Provincia
        logisticaTable.getColumnModel().getColumn(9).setPreferredWidth(90);   // Distrito
        logisticaTable.getColumnModel().getColumn(10).setPreferredWidth(150); // Dirección
        logisticaTable.getColumnModel().getColumn(11).setPreferredWidth(120); // Transportadora
        logisticaTable.getColumnModel().getColumn(12).setPreferredWidth(100); // Nro Tracking
        logisticaTable.getColumnModel().getColumn(13).setPreferredWidth(100); // Cod Tracking

        System.out.println("Tabla de logística inicializada");
    }

// Método para inicializar la tabla de inventario
    public void initializeInventarioTable() {
        // Definir las columnas según la imagen de inventario
        String[] columnNames = {
            "Nro Orden",
            "Fecha",
            "SKU",
            "Producto",
            "Movimiento",
            "Cantidad"
        };

        // Crear modelo de tabla sin datos inicialmente
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        inventarioTable.setModel(model);

        // Ajustar anchos de columnas (opcional)
        inventarioTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Nro Orden
        inventarioTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
        inventarioTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // SKU
        inventarioTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Producto
        inventarioTable.getColumnModel().getColumn(4).setPreferredWidth(90);  // Movimiento
        inventarioTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Cantidad

        System.out.println("Tabla de inventario inicializada");
    }

// Método para inicializar la tabla de reportes (conteo de inventario)
    public void initializeReportesTable() {
        // Definir las columnas según la imagen de reportes
        String[] columnNames = {
            "SKU",
            "Producto",
            "Precio",
            "Cantidad disponible"
        };

        // Crear modelo de tabla sin datos inicialmente
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        conteoTable.setModel(model);

        // Ajustar anchos de columnas (opcional)
        conteoTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // SKU
        conteoTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Producto
        conteoTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
        conteoTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Cantidad disponible

        System.out.println("Tabla de reportes inicializada");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        ventasPane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ventasTable = new javax.swing.JTable();
        agregarpedidoButton = new javax.swing.JButton();
        editarPedidoVentasButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        estadoVentasCombo = new javax.swing.JComboBox<>();
        cambiarEstadoVentasButton = new javax.swing.JButton();
        salirButton = new javax.swing.JButton();
        logisticaPane = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logisticaTable = new javax.swing.JTable();
        updateShalom = new javax.swing.JButton();
        editarInfoLogisticaButton = new javax.swing.JButton();
        verifyPaymentButton = new javax.swing.JButton();
        inventarioPane = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        inventarioTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        skuInvCombo = new javax.swing.JComboBox<>();
        movimientoCombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cantidadInvField1 = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        registrarMovimientoButton1 = new javax.swing.JButton();
        nuevoProductoButton = new javax.swing.JButton();
        reportePane = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        conteoTable = new javax.swing.JTable();

        ventasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(ventasTable);

        agregarpedidoButton.setText("Agregar pedido");
        agregarpedidoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarpedidoButtonActionPerformed(evt);
            }
        });

        editarPedidoVentasButton.setText("Editar pedido seleccionado");
        editarPedidoVentasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarPedidoVentasButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Cambiar estado del pedido seleccionado:");

        jLabel30.setText("Nuevo estado:");

        cambiarEstadoVentasButton.setText("Cambiar estado");
        cambiarEstadoVentasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarEstadoVentasButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel30)
                        .addGap(18, 18, 18)
                        .addComponent(estadoVentasCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(cambiarEstadoVentasButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(estadoVentasCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cambiarEstadoVentasButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        salirButton.setText("Salir");
        salirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ventasPaneLayout = new javax.swing.GroupLayout(ventasPane);
        ventasPane.setLayout(ventasPaneLayout);
        ventasPaneLayout.setHorizontalGroup(
            ventasPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(ventasPaneLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(salirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(184, 184, 184)
                .addGroup(ventasPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(agregarpedidoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editarPedidoVentasButton, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        ventasPaneLayout.setVerticalGroup(
            ventasPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventasPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(ventasPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ventasPaneLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(agregarpedidoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editarPedidoVentasButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ventasPaneLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ventasPaneLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ventas", ventasPane);

        logisticaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(logisticaTable);

        updateShalom.setText("Actualizar estados SHALOM");
        updateShalom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateShalomActionPerformed(evt);
            }
        });

        editarInfoLogisticaButton.setText("Editar información de envío");
        editarInfoLogisticaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarInfoLogisticaButtonActionPerformed(evt);
            }
        });

        verifyPaymentButton.setText("Verificar información de pago");
        verifyPaymentButton.setAlignmentX(0.5F);

        javax.swing.GroupLayout logisticaPaneLayout = new javax.swing.GroupLayout(logisticaPane);
        logisticaPane.setLayout(logisticaPaneLayout);
        logisticaPaneLayout.setHorizontalGroup(
            logisticaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
            .addGroup(logisticaPaneLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(logisticaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logisticaPaneLayout.createSequentialGroup()
                        .addComponent(verifyPaymentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(657, Short.MAX_VALUE))
                    .addGroup(logisticaPaneLayout.createSequentialGroup()
                        .addComponent(editarInfoLogisticaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateShalom, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );
        logisticaPaneLayout.setVerticalGroup(
            logisticaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logisticaPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(logisticaPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateShalom, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editarInfoLogisticaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(verifyPaymentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Logística", logisticaPane);

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("Movimientos");

        inventarioTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(inventarioTable);

        jLabel1.setText("SKU");

        skuInvCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skuInvComboActionPerformed(evt);
            }
        });

        jLabel3.setText("Movimiento");

        jLabel4.setText("Cantidad");

        jLabel7.setText("Registrar movimiento");

        registrarMovimientoButton1.setText("Registrar movimiento");
        registrarMovimientoButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarMovimientoButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(movimientoCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cantidadInvField1)
                    .addComponent(skuInvCombo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(86, 86, 86))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap(92, Short.MAX_VALUE)
                    .addComponent(registrarMovimientoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(55, 55, 55)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(skuInvCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(movimientoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cantidadInvField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap(193, Short.MAX_VALUE)
                    .addComponent(registrarMovimientoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(7, 7, 7)))
        );

        nuevoProductoButton.setText("Nuevo producto");
        nuevoProductoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoProductoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventarioPaneLayout = new javax.swing.GroupLayout(inventarioPane);
        inventarioPane.setLayout(inventarioPaneLayout);
        inventarioPaneLayout.setHorizontalGroup(
            inventarioPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventarioPaneLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(inventarioPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addGroup(inventarioPaneLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nuevoProductoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        inventarioPaneLayout.setVerticalGroup(
            inventarioPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventarioPaneLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(inventarioPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(inventarioPaneLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(nuevoProductoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Inventario", inventarioPane);

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setText("Conteo de inventario:");

        conteoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(conteoTable);

        javax.swing.GroupLayout reportePaneLayout = new javax.swing.GroupLayout(reportePane);
        reportePane.setLayout(reportePaneLayout);
        reportePaneLayout.setHorizontalGroup(
            reportePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportePaneLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(reportePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(429, Short.MAX_VALUE))
        );
        reportePaneLayout.setVerticalGroup(
            reportePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportePaneLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(147, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Reportes", reportePane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarpedidoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarpedidoButtonActionPerformed
        // TODO add your handling code here:

        // Verificar que el controller esté inicializado
        if (controller == null) {
            System.err.println("Error: Controller no inicializado");
            return;
        }

        // Llamar al controller para abrir la vista de agregar pedido
        controller.abrirAgregarPedidoView();
    }//GEN-LAST:event_agregarpedidoButtonActionPerformed

    private void editarPedidoVentasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarPedidoVentasButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editarPedidoVentasButtonActionPerformed

    private void cambiarEstadoVentasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarEstadoVentasButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cambiarEstadoVentasButtonActionPerformed

    private void salirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirButtonActionPerformed
        // TODO add your handling code here:

        this.dispose();
    }//GEN-LAST:event_salirButtonActionPerformed

    private void updateShalomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateShalomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateShalomActionPerformed

    private void editarInfoLogisticaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarInfoLogisticaButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editarInfoLogisticaButtonActionPerformed

    private void skuInvComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skuInvComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_skuInvComboActionPerformed

    private void registrarMovimientoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarMovimientoButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registrarMovimientoButton1ActionPerformed

    private void nuevoProductoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoProductoButtonActionPerformed

        // Verificar que el controller esté inicializado
        if (controller == null) {
            System.err.println("Error: Controller no inicializado");
            return;
        }

        // Llamar al controller para abrir la vista de crear producto
        controller.abrirCreateProductView();
    }//GEN-LAST:event_nuevoProductoButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarpedidoButton;
    private javax.swing.JButton cambiarEstadoVentasButton;
    private javax.swing.JSpinner cantidadInvField1;
    private javax.swing.JTable conteoTable;
    private javax.swing.JButton editarInfoLogisticaButton;
    private javax.swing.JButton editarPedidoVentasButton;
    private javax.swing.JComboBox<String> estadoVentasCombo;
    private javax.swing.JPanel inventarioPane;
    private javax.swing.JTable inventarioTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel logisticaPane;
    private javax.swing.JTable logisticaTable;
    private javax.swing.JComboBox<String> movimientoCombo;
    private javax.swing.JButton nuevoProductoButton;
    private javax.swing.JButton registrarMovimientoButton1;
    private javax.swing.JPanel reportePane;
    private javax.swing.JButton salirButton;
    private javax.swing.JComboBox<String> skuInvCombo;
    private javax.swing.JButton updateShalom;
    private javax.swing.JPanel ventasPane;
    private javax.swing.JTable ventasTable;
    private javax.swing.JButton verifyPaymentButton;
    // End of variables declaration//GEN-END:variables
}
