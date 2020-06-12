
package SAYAV2.SAYAV2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import SAYAV2.SAYAV2.mensajeria.TipoMensaje;


/**
 * <p>Java class for tipos complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tipos"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.example.org/Mensaje}TipoMensaje"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipos", namespace = "http://www.example.org/TiposMensajes")
public class Tipos
    extends TipoMensaje
{


}
