package Model;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 * @web http://www.jc-mouse.net
 * @author Mouse
 */
public class FormatoTabla extends DefaultTableCellRenderer{

    private String columna_patron ;
    private String columna_patron2 ;
    public FormatoTabla(String Colpatron_one, String Colpatron_two)
    {
        this.columna_patron = Colpatron_one;
        this.columna_patron2 = Colpatron_two;
    }

    @Override
    public Component getTableCellRendererComponent ( JTable table, Object value, boolean selected, boolean focused, int row, int column )
    {        
        setBackground(Color.white);//color de fondo
        table.setForeground(Color.black);//color de texto
        //Si la celda corresponde a una fila con estado FALSE, se cambia el color de fondo a rojo
        if( table.getValueAt(row,column).equals(columna_patron) )
        {
                    setBackground(new java.awt.Color(102, 187, 106));
           setForeground(new java.awt.Color(255, 255, 255));
        }else if(table.getValueAt(row,column).equals(columna_patron2)){

            setBackground(new java.awt.Color(239, 83, 88));
          setForeground(new java.awt.Color(255, 255, 255));
        }else{
            setForeground(new java.awt.Color(0, 0, 0));
        }
        
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        return this;
 }

}
