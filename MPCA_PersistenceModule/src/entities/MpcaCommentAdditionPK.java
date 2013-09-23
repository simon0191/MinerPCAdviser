/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Antonio
 */
@Embeddable
public class MpcaCommentAdditionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MPCA_COMMENT_COMMENT_ID")
    private long mpcaCommentCommentId;
    @Basic(optional = false)
    @Column(name = "MPCA_ADDITION_TYPE_ADD_ID")
    private long mpcaAdditionTypeAddId;

    public MpcaCommentAdditionPK() {
    }

    public MpcaCommentAdditionPK(long mpcaCommentCommentId, long mpcaAdditionTypeAddId) {
        this.mpcaCommentCommentId = mpcaCommentCommentId;
        this.mpcaAdditionTypeAddId = mpcaAdditionTypeAddId;
    }

    public long getMpcaCommentCommentId() {
        return mpcaCommentCommentId;
    }

    public void setMpcaCommentCommentId(long mpcaCommentCommentId) {
        this.mpcaCommentCommentId = mpcaCommentCommentId;
    }

    public long getMpcaAdditionTypeAddId() {
        return mpcaAdditionTypeAddId;
    }

    public void setMpcaAdditionTypeAddId(long mpcaAdditionTypeAddId) {
        this.mpcaAdditionTypeAddId = mpcaAdditionTypeAddId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mpcaCommentCommentId;
        hash += (int) mpcaAdditionTypeAddId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MpcaCommentAdditionPK)) {
            return false;
        }
        MpcaCommentAdditionPK other = (MpcaCommentAdditionPK) object;
        if (this.mpcaCommentCommentId != other.mpcaCommentCommentId) {
            return false;
        }
        if (this.mpcaAdditionTypeAddId != other.mpcaAdditionTypeAddId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MpcaCommentAdditionPK[ mpcaCommentCommentId=" + mpcaCommentCommentId + ", mpcaAdditionTypeAddId=" + mpcaAdditionTypeAddId + " ]";
    }
    
}
