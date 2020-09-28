package herramientaTesting;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import clase.Clase;
import metodo.Metodo;
import javax.swing.JButton;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static GUI frame;
	private JLabel lblLineasDeCodigo;	
	private JLabel lblLineasEnBlanco;	
	private JLabel lblPorcentajeComentarios;
	private JLabel lblLineasTotales;
	private File archivo;
	private Analizable carpetaBase;
	private JTree arbolDirectorios;
	private JScrollPane scrollPaneArbol;
	//
	private JLabel labelHalsteadLong;
	private JLabel labelHalsteadVol;
	private JLabel labelFanOut;
	private JLabel labelFanIn;
	private JLabel labelComplejidad;
	//Listas
	private JList<Metodo> listaMetodos;
	private JList<Clase> listaClases;
	private DefaultListModel<Clase> listModelClases;
	private DefaultListModel<Metodo> listModelMetodos;
	private boolean permitirAnalizar;
	private JLabel warningComplejidad;
	
	private int severidadMinimaComplejidad = 10;
	
	//Application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Constructor
	public GUI() {
		setResizable(false);
		setTitle("Analizador de Codigo");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frame.dispose();
			}
		});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 680, 620);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		permitirAnalizar=false;
		
		JButton btnNewButton = new JButton("Seleccionar carpeta");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(frame);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            archivo = fc.getSelectedFile();
		            analizarPath(archivo);
		            permitirAnalizar=true;
		        }
			}
		});
		btnNewButton.setBounds(10, 10, 368, 32);
		contentPane.add(btnNewButton);
		
		JLabel lblLneasDeCdigo = new JLabel("L\u00EDneas de c\u00F3digo");
		lblLneasDeCdigo.setBounds(443, 210, 172, 14);
		contentPane.add(lblLneasDeCdigo);
		
		JLabel lblLneasEnBlanco = new JLabel("Lineas en blanco");
		lblLneasEnBlanco.setBounds(443, 235, 172, 14);
		contentPane.add(lblLneasEnBlanco);
		
		JLabel lblComentariosDeLnea = new JLabel("Lineas de Comentarios");
		lblComentariosDeLnea.setBounds(443, 260, 172, 14);
		contentPane.add(lblComentariosDeLnea);
		
		JLabel lblLneasTotales = new JLabel("Lineas totales");
		lblLneasTotales.setBounds(443, 185, 172, 14);
		contentPane.add(lblLneasTotales);
		
		lblLineasTotales = new JLabel("");
		lblLineasTotales.setBackground(Color.GRAY);
		lblLineasTotales.setBounds(625, 185, 121, 14);
		contentPane.add(lblLineasTotales);
		
		lblLineasDeCodigo = new JLabel("");
		lblLineasDeCodigo.setBounds(625, 210, 121, 14);
		contentPane.add(lblLineasDeCodigo);
		
		lblLineasEnBlanco = new JLabel("");
		lblLineasEnBlanco.setBounds(625, 235, 121, 14);
		contentPane.add(lblLineasEnBlanco);
		
		lblPorcentajeComentarios = new JLabel("");
		lblPorcentajeComentarios.setBounds(625, 260, 121, 14);
		contentPane.add(lblPorcentajeComentarios);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(443, 223, 172, 14);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(443, 248, 172, 14);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(443, 273, 172, 8);
		contentPane.add(separator_2);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(443, 198, 172, 14);
		contentPane.add(separator_4);
		
		JScrollPane scrollPaneMetodos = new JScrollPane();
		scrollPaneMetodos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneMetodos.setBounds(238, 83, 140, 472);
		contentPane.add(scrollPaneMetodos);
		
		listModelMetodos = new DefaultListModel<Metodo>();
		listaMetodos = new JList<Metodo>(listModelMetodos);
		listaMetodos.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()==false)
					actualizarVistaCodigo(listaMetodos.getSelectedValue());
			}
		});
		listaMetodos.setBackground(SystemColor.control);
		listaMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//scrollPaneMetodos.add(listaMetodos);
		scrollPaneMetodos.setViewportView(listaMetodos);
		
		JLabel lblMetodos = new JLabel("Metodos:");
		lblMetodos.setForeground(Color.BLACK);
		lblMetodos.setBounds(238, 58, 67, 14);
		contentPane.add(lblMetodos);
		
		JScrollPane scrollPaneClases = new JScrollPane();
		scrollPaneClases.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneClases.setBounds(10, 329, 200, 227);
		contentPane.add(scrollPaneClases);
		
		listModelClases = new DefaultListModel<Clase>();
		listaClases = new JList<Clase>(listModelClases);
		listaClases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaClases.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()==false)
					actualizarListaMetodos(listaClases.getSelectedValue());
			}
		});
		listaClases.setBackground(SystemColor.control);
		listaMetodos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneClases.add(listaClases);
		scrollPaneClases.setRowHeaderView(listaClases);
		
		JLabel lblClases = new JLabel("Seleccionar clase:");
		lblClases.setForeground(Color.BLACK);
		lblClases.setBounds(17, 295, 200, 14);
		contentPane.add(lblClases);
		
		JLabel lblAnalisis = new JLabel("Resultados:");
		lblAnalisis.setForeground(Color.BLACK);
		lblAnalisis.setBounds(443, 111, 115, 14);
		contentPane.add(lblAnalisis);
		
		JLabel labelExplorador = new JLabel("Explorador:");
		labelExplorador.setForeground(Color.BLACK);
		labelExplorador.setBounds(10, 46, 206, 20);
		contentPane.add(labelExplorador);
		
		JLabel lblComplejidadCiclomatica = new JLabel("Complejidad ciclomatica");
		lblComplejidadCiclomatica.setBounds(443, 285, 172, 14);
		contentPane.add(lblComplejidadCiclomatica);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(443, 298, 172, 14);
		contentPane.add(separator_6);
		
		JLabel lblFanIn = new JLabel("Fan In");
		lblFanIn.setBounds(443, 310, 172, 14);
		contentPane.add(lblFanIn);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(443, 323, 172, 14);
		contentPane.add(separator_7);
		
		JLabel lblFanOut = new JLabel("Fan Out");
		lblFanOut.setBounds(443, 334, 172, 14);
		contentPane.add(lblFanOut);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setBounds(443, 347, 172, 14);
		contentPane.add(separator_8);
		
		JLabel lblHalsteadLongitud = new JLabel("Halstead longitud");
		lblHalsteadLongitud.setBounds(443, 358, 172, 14);
		contentPane.add(lblHalsteadLongitud);
		
		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(443, 371, 172, 14);
		contentPane.add(separator_9);
		
		JLabel lblHalsteadVolumen = new JLabel("Halstead volumen");
		lblHalsteadVolumen.setBounds(443, 383, 172, 14);
		contentPane.add(lblHalsteadVolumen);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setBounds(443, 396, 172, 14);
		contentPane.add(separator_10);
		
		labelComplejidad = new JLabel("");
		labelComplejidad.setBounds(625, 285, 121, 14);
		contentPane.add(labelComplejidad);
		
		labelFanIn = new JLabel("");
		labelFanIn.setBounds(625, 310, 121, 14);
		contentPane.add(labelFanIn);
		
		labelFanOut = new JLabel("");
		labelFanOut.setBounds(625, 334, 121, 14);
		contentPane.add(labelFanOut);
		
		labelHalsteadLong = new JLabel("");
		labelHalsteadLong.setBounds(625, 358, 121, 14);
		contentPane.add(labelHalsteadLong);
		
		labelHalsteadVol = new JLabel("");
		labelHalsteadVol.setBounds(625, 383, 121, 14);
		contentPane.add(labelHalsteadVol);
		
		scrollPaneArbol = new JScrollPane();
		scrollPaneArbol.setBounds(10, 82, 200, 194);
		contentPane.add(scrollPaneArbol);
		
		warningComplejidad = new JLabel("");
		warningComplejidad.setBounds(590, 276, 22, 22);
		contentPane.add(warningComplejidad);
		
	}	
	private void analizarPath(File file) {
		//Verificar si es una carpeta o un archivo
		if(scrollPaneArbol!=null)
			contentPane.remove(scrollPaneArbol);
		if(arbolDirectorios!=null)
			scrollPaneArbol.remove(arbolDirectorios);
        if(file.isDirectory()){
        	carpetaBase = new Carpeta(file);
        }
        else{
        	carpetaBase = new Archivo(file);
        }
        arbolDirectorios = new JTree(carpetaBase.colocarEnArbol(new DefaultMutableTreeNode()));
        arbolDirectorios.setSelectionRow(0); //Selecciona la raiz del arbol por defecto
        scrollPaneArbol = new JScrollPane();
        scrollPaneArbol.setBounds(10, 62, 200, 196);
		contentPane.add(scrollPaneArbol);
		scrollPaneArbol.add(arbolDirectorios);
		scrollPaneArbol.setViewportView(arbolDirectorios);
        carpetaBase.analizar();
        actualizarLineas(carpetaBase);
        arbolDirectorios.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
            	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) arbolDirectorios.getLastSelectedPathComponent();
            	Analizable an = (Analizable) selectedNode.getUserObject();
            	actualizarLineas(an);
            	if(!(an.isDirectory()))
            		actualizarListaClases((Archivo)an);
            }
        });
        arbolDirectorios.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
				arbolDirectorios.setSelectionPath(e.getPath());
			}
			public void treeExpanded(TreeExpansionEvent e) {
			}
		});
	}
	
	private void actualizarLineas(Analizable a){
        lblLineasDeCodigo.setText(String.valueOf(a.getCantidadLineasDeCodigo()));
    	lblLineasEnBlanco.setText(String.valueOf(a.getCantidadLineasEnBlanco()));
    	lblPorcentajeComentarios.setText(String.valueOf(a.getCantidadLineasComentadas()));
    	lblLineasTotales.setText(String.valueOf(a.getCantidadDeLineas()));
    	labelComplejidad.setText("");
    	labelFanIn.setText("");
	}
	
	
	
	private void actualizarListaClases(Archivo a){
		System.out.println("ACTUALIZANDO LISTA DE CLASES");
		listModelClases.clear();
		if(a.getClase()==null)
			System.out.println("CLASE NULA");
		else {
			System.out.println("CLASE: "+a.getClase().getNombre());
			listModelClases.addElement(a.getClase());
		}
	}
	
	private void actualizarListaMetodos(Clase c){
		listModelMetodos.clear();
		if(c==null){
			System.out.println("asd");
		}
		else		
			for(Metodo m : c.getMetodos()){
				listModelMetodos.addElement(m);
			}
	}
	
	private void actualizarVistaCodigo(Metodo m){
		if(m!=null){
			lblLineasDeCodigo.setText(String.valueOf(m.getCantidadLineasDeCodigo()));
	    	lblLineasEnBlanco.setText(String.valueOf(m.getCantidadLineasEnBlanco()));
	    	lblPorcentajeComentarios.setText(String.valueOf(m.getCantidadLineasComentadas()));
	    	lblLineasTotales.setText(String.valueOf(m.getCantidadDeLineas()));
	    	labelComplejidad.setText(String.valueOf(m.getComplejidadCiclomatica()));
	    	labelFanIn.setText(String.valueOf(m.getfanIn()));
	    	labelFanOut.setText(String.valueOf(m.getFanOut()));
	    	labelHalsteadLong.setText(String.valueOf(m.getHalsteadLongitud()));;
	    	labelHalsteadVol.setText(String.format("%.2f",m.getHalsteadVolumen()));
			if(m.getComplejidadCiclomatica() > severidadMinimaComplejidad) {
				ImageIcon ico = new ImageIcon(".\\media\\img\\warning_icon.png");
				warningComplejidad.setIcon(ico);
			}
			else {
				warningComplejidad.setIcon(null);
			}
			warningComplejidad.repaint();
		}
	}
}