/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Antonio
 */
@Entity
@Table(name = "MPCA_COMMENT_ADDITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MpcaCommentAddition.findAll", query = "SELECT m FROM MpcaCommentAddition m"),
    @NamedQuery(name = "MpcaCommentAddition.findByMpcaCommentCommentId", query = "SELECT m FROM MpcaCommentAddition m WHERE m.mpcaCommentAdditionPK.mpcaCommentCommentId = :mpcaCommentCommentId"),
    @NamedQuery(name = "MpcaCommentAddition.findByMpcaAdditionTypeAddId", query = "SELECT m FROM MpcaCommentAddition m WHERE m.mpcaCommentAdditionPK.mpcaAdditionTypeAddId = :mpcaAdditionTypeAddId"),
    @NamedQuery(name = "MpcaCommentAddition.findByValue", query = "SELECT m FROM MpcaCommentAddition m WHERE m.value = :value")})
public class MpcaCommentAddition implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MpcaCommentAdditionPK mpcaCommentAdditionPK;
    @Column(name = "VALUE")
    private String value;
    @JoinColumn(name = "MPCA_COMMENT_COMMENT_ID", referencedColumnName = "COMMENT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaComment mpcaComment;
    @JoinColumn(name = "MPCA_ADDITION_TYPE_ADD_ID", referencedColumnName = "ADD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MpcaAdditionType mpcaAdditionType;

    public MpcaCommentAddition() {
    }

    public MpcaCommentAddition(MpcaCommentAdditionPK mpcaCommentAdditionPK) {
        this.mpcaCommentAdditionPK = mpcaCommentAdditionPK;
    }

    public MpcaCommentAddition(long mpcaCommentCommentId, long mpcaAdditionTypeAddId) {
        this.mpcaCommentAdditionPK = new MpcaCommentAdditionPK(mpcaCommentCommentId, mpcaAdditionTypeAddId);
    }

    public MpcaCommentAdditionPK getMpcaCommentAdditionPK() {
        return mpcaCommentAdditionPK;
    }

    public void setMpcaCommentAdditionPK(MpcaCommentAdditionPK mpcaCommentAdditionPK) {
        this.mpcaCommentAdditionPK = mpcaCommentAdditionPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MpcaComment getMpcaComment() {
        return mpcaComment;
    }

    public void setMpcaComment(MpcaComment mpcaComment) {
        this.mpcaComment = mpcaComment;
    }

    public MpcaAdditionType getMpcaAdditionType() {
        return mpcaAdditionType;
    }

    public void setMpcaAdditionType(MpcaAdditionType mpcaAdditionType) {
        this.mpcaAdditionType = mpcaAdditionType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpcaCommentAdditionPK != null ? mpcaCommentAdditionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaCommentAddition)) {
            return false;
        }
        MpcaCommentAddition other = (MpcaCommentAddition) object;
        if ((this.mpcaCommentAdditionPK == null && other.mpcaCommentAdditionPK != null) || (this.mpcaCommentAdditionPK != null && !this.mpcaCommentAdditionPK.equals(other.mpcaCommentAdditionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaCommentAddition[ mpcaCommentAdditionPK=" + mpcaCommentAdditionPK + " ]";
    }
    
}
