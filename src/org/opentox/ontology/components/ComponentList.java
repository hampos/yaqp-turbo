/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */


package org.opentox.ontology.components;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.UriListObject;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ComponentList<H extends YaqpComponent> extends YaqpComponent implements List<H>{

    private ArrayList<H> componentList = new ArrayList<H>();

//    public void add(H component){
//        componentList.add(component);
//    }

//    public void addAll(Collection<H> componentList){
//        componentList.addAll(componentList);
//    }

//    public void clearList(){
//        componentList.clear();
//    }

    public ArrayList<H> getComponentList() {
        return componentList;
    }

    public void setComponentList(ArrayList<H> componentList) {
        this.componentList = componentList;
    }

    @Override
    public PDFObject getPDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RDFObject getRDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getTag() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UriListObject getUriList() throws YaqpException{
        ArrayList<URI> uriList = new ArrayList<URI>();
        for (H component : componentList){
            uriList.add(component.uri());
        }
        return new UriListObject(uriList);
    }

    public int size() {
        return componentList.size();
    }

    public boolean isEmpty() {
        return componentList.isEmpty();
    }

    public boolean contains(H o) {
        return componentList.contains(o);
    }

    public Iterator<H> iterator() {
        return componentList.iterator();
    }

    public Object[] toArray() {
        return componentList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return componentList.toArray(a);
    }

    public boolean add(H e) {
        return componentList.add(e);
    }

    public boolean remove(H o) {
        return componentList.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return componentList.containsAll(c);
    }

    public boolean addAll(Collection<? extends H> c) {
        return componentList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends H> c) {
        return componentList.addAll(index, c);
    }

    public H get(int i){
        return componentList.get(i);
    }

    public boolean removeAll(Collection<?> c) {
        return componentList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return componentList.retainAll(c);
    }

    public void clear() {
         componentList.clear();
    }

    public H set(int index, H element) {
        return componentList.set(index, element);
    }

    public void add(int index, H element) {
        componentList.add(index, element);
    }

    public H remove(int index) {
        return componentList.remove(index);
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator<H> listIterator() {
        return componentList.listIterator();
    }

    public ListIterator<H> listIterator(int index) {
        return componentList.listIterator(index);
    }

    public List<H> subList(int fromIndex, int toIndex) {
        return componentList.subList(fromIndex, toIndex);
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}