/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_LABEL_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaLabelType.findAll", query = "SELECT m FROM MpcaLabelType m"),
    @NamedQuery(name = "MpcaLabelType.findByLabelId", query = "SELECT m FROM MpcaLabelType m WHERE m.labelId = :labelId"),
    @NamedQuery(name = "MpcaLabelType.findByLabelName", query = "SELECT m FROM MpcaLabelType m WHERE m.labelName = :labelName")})
public class MpcaLabelType implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "LABEL_ID")
    @SequenceGenerator(name="SEQ_LABEL_TYPE", sequenceName = "MPCA_LABEL_TYPE_LABEL_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_LABEL_TYPE")
    private BigDecimal labelId;
    @Basic(optional = false)
    @Column(name = "LABEL_NAME")
    private String labelName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "labelId", fetch = FetchType.LAZY)
    private List<MpcaCommentIndex> mpcaCommentIndexList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "labelId", fetch = FetchType.LAZY)
    private List<MpcaProductIndex> mpcaProductIndexList;

    public MpcaLabelType() {
    }

    public MpcaLabelType(BigDecimal labelId) {
        this.labelId = labelId;
    }

    public MpcaLabelType(BigDecimal labelId, String labelName) {
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public BigDecimal getLabelId() {
        return labelId;
    }

    public void setLabelId(BigDecimal labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @XmlTransient
    public List<MpcaCommentIndex> getMpcaCommentIndexList() {
        return mpcaCommentIndexList;
    }

    public void setMpcaCommentIndexList(List<MpcaCommentIndex> mpcaCommentIndexList) {
        this.mpcaCommentIndexList = mpcaCommentIndexList;
    }

    @XmlTransient
    public List<MpcaProductIndex> getMpcaProductIndexList() {
        return mpcaProductIndexList;
    }

    public void setMpcaProductIndexList(List<MpcaProductIndex> mpcaProductIndexList) {
        this.mpcaProductIndexList = mpcaProductIndexList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (labelId != null ? labelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaLabelType)) {
            return false;
        }
        MpcaLabelType other = (MpcaLabelType) object;
        if ((this.labelId == null && other.labelId != null) || (this.labelId != null && !this.labelId.equals(other.labelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaLabelType[ labelId=" + labelId + " ]";
    }
    
}
