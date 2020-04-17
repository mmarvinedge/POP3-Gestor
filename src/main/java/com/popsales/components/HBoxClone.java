/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.HBox;

/**
 *
 * @author Renato
 */
public class HBoxClone extends HBox implements Cloneable {

    @Override
    public HBoxClone clone() {
        HBoxClone clone = null;
        try {
            clone = (HBoxClone) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(HBoxClone.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clone;
    }
}
