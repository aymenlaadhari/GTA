package managedBeans.gtaparam;

import java.io.Serializable;

public class Field implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_sName;

    public void setName(String p_sName)
    {
        m_sName = p_sName;
    }

    public String getName()
    {
        return m_sName;
    }
}
