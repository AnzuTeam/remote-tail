package com.prhythm.app.remotetail.models;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 封裝資料
 * Created by nanashi07 on 15/12/30.
 */
@XmlRootElement(name = "wrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataWrapper {

    /**
     * 取檔案讀取設定
     *
     * @param file 設定檔 app.xml
     * @return
     * @throws JAXBException
     */
    public static DataWrapper read(File file) throws JAXBException {
        if (file == null || !file.exists()) return new DataWrapper();
        JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (DataWrapper) unmarshaller.unmarshal(file);
    }

    /**
     * 視窗屬性
     */
    Window window;

    /**
     * 偏好選項
     */
    Preference preference;

    /**
     * 顯著標示
     */
    HighLights highLights;

    /**
     * 記錄的 {@link Server}
     */
    @XmlElementWrapper(name = "servers")
    @XmlElement(name = "server")
    List<Server> servers;

    public DataWrapper() {
        this.window = new Window();
        this.preference = new Preference();
        this.highLights = new HighLights();
        this.servers = new ArrayList<>();
    }

    /**
     * 儲存設定
     *
     * @param file 目標檔 app.xml
     * @throws JAXBException
     */
    public void save(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(DataWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, file);
    }

    // getter & setter

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public HighLights getHighLights() {
        return highLights;
    }

    public void setHighLights(HighLights highLights) {
        this.highLights = highLights;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

}
