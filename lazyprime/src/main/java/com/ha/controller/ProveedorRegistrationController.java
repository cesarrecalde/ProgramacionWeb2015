package com.ha.controller;

import com.ha.data.ClientRepository;
import com.ha.data.ProviderRepository;
import com.ha.model.Client;
import com.ha.model.Provider;
import com.ha.service.ClientMassiveRegistration;
import com.ha.service.ProviderMassiveRegistration;
import com.ha.util.CSVFileReadingException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.logging.Logger;

/**
 * Created by cesar on 29/10/15.
 */
@ViewScoped
@ManagedBean(name = "proveedorRC")
public class ProveedorRegistrationController {
    @Inject
    private ProviderRepository providerRepository;

    @Inject
    private ProviderMassiveRegistration providerMassiveRegistration;

    @Inject
    private Logger log;

    private Provider newProvider;

    @PostConstruct
    private void init(){
        this.newProvider = new Provider();
    }

    public void registerProvider(){
        this.providerRepository.register( this.newProvider );
        this.newProvider = new Provider();
    }

    public void massiveRegistration( FileUploadEvent event ) throws CSVFileReadingException,IOException {
        UploadedFile file = event.getFile();
        String fileName = file.getFileName();
        System.out.println("***" + fileName + "***");
        String filePath = "/home/cesar/Escritorio/" + fileName;

        File archivoGuardado = new File( filePath );

        InputStream in = file.getInputstream();
        OutputStream out = new FileOutputStream( archivoGuardado );

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }

        in.close();
        out.close();

        FileReader fr = new FileReader( archivoGuardado );

        try {
            providerMassiveRegistration.massiveRegistration( fr );
            fr.close();
        } catch (CSVFileReadingException e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Error!",
                                    "\n" + e.getMessage()
                            )
                    );
        }
    }

    public Provider getNewProvider() {
        return newProvider;
    }
    public void setNewProvider( Provider p) {
        this.newProvider = p;
    }
}
