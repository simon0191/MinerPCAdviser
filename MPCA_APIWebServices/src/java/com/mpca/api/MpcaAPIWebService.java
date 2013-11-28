/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpca.api;

import com.mpca.controllers.MpcaProductAdditionJpaController;
import com.mpca.entities.MpcaProductAddition;
import com.mpca.filters.MpcaFilter;
import com.mpca.utils.MpcaIConstants;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.naming.NamingException;
/*
import model.utils.MpcaIConstants;*/

/**
 *
 * @author Antonio
 */
@WebService(serviceName = "MpcaAPIWebService")
public class MpcaAPIWebService {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getFilters")
    public List<MpcaFilter> getFilters() throws NamingException {
        MpcaFilter<String> fBrand = new MpcaFilter<String>("Brand");
        MpcaFilter<Integer> fRAM = new MpcaFilter<Integer>("RAM");
        MpcaFilter<Integer> fHardDrive = new MpcaFilter<Integer>("Hard Drive");
        
        MpcaProductAdditionJpaController pac = new MpcaProductAdditionJpaController();
        List<MpcaProductAddition> brands = pac.findByType(MpcaIConstants.BRAND_TAG);
        List<MpcaProductAddition> rams = pac.findByType(MpcaIConstants.RAM_TAG);
        List<MpcaProductAddition> hds = pac.findByType(MpcaIConstants.HARD_DRIVE_TAG);
        
        for (MpcaProductAddition brand : brands) {
            fBrand.addValue(brand.getValue());
        }
        
        for (MpcaProductAddition ram : rams) {
            String[] data = ram.getValue().split(" +");
            fRAM.addValue(Integer.parseInt(data[0]));
        }
        
        for (MpcaProductAddition hd : hds) {
            String[] d = hd.getValue().split(" +");
            if(d[1].toLowerCase().contains("t")) {
                d[0] = (Integer.parseInt(d[0]) * 1024) + "";
            }
            fHardDrive.addValue(Integer.parseInt(d[0]));
        }

        List<MpcaFilter> fs = new ArrayList<MpcaFilter>();
        fs.add(fBrand);
        fs.add(fRAM);
        fs.add(fHardDrive);
        return fs;
    }
}
