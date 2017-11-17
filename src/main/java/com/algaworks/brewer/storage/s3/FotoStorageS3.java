package com.algaworks.brewer.storage.s3;

import com.algaworks.brewer.storage.FotoStorage;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Profile("producao")
@Component
public class FotoStorageS3 implements FotoStorage {

    private static final String BUCKET_NAME = "agillesbrewer";
    private static final Logger logger = LoggerFactory.getLogger(FotoStorageS3.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public String salvar(MultipartFile[] files) {
        String novoNome = null;
        if (files != null && files.length > 0) {
            MultipartFile arquivo = files[0];
            novoNome = renomearArquivo(arquivo.getOriginalFilename());
            try {
                AccessControlList acs = new AccessControlList();

                enviarFoto(novoNome, arquivo, acs);

                enviarThumbnail(novoNome, arquivo, acs);

            } catch (IOException e) {
                throw new RuntimeException("Erro salvando arquivo no s3 - ", e);
            }
        }
        return novoNome;

    }



    @Override
    public byte[] recuperar(String foto) {
        InputStream is = amazonS3.getObject(BUCKET_NAME, foto).getObjectContent();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            logger.error("Não conseguiu recuperar foto do s3", e);
        }
        return null;
    }

    @Override
    public byte[] recuperarThumbnail(String fotoCerveja) {
        return recuperar(FotoStorage.THUMBNAIL_PREFIX + fotoCerveja);
    }

    @Override
    public void excluir(String foto) {
        amazonS3.deleteObjects(new DeleteObjectsRequest(BUCKET_NAME).withKeys(foto, THUMBNAIL_PREFIX + foto));
    }

    @Override
    public String getUrl(String foto) {
        if (!StringUtils.isEmpty(foto)) {
            return "https://s3-sa-east-1.amazonaws.com/agillesbrewer/" + foto;
        }
        return null;
    }


    private ObjectMetadata enviarFoto(String novoNome, MultipartFile arquivo, AccessControlList acs) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(arquivo.getContentType());
        objectMetadata.setContentLength(arquivo.getSize());
        acs.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, novoNome, arquivo.getInputStream(), objectMetadata)
                .withAccessControlList(acs));
        return objectMetadata;
    }

    private void enviarThumbnail(String novoNome, MultipartFile arquivo, AccessControlList acs) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(arquivo.getInputStream()).size(40, 68).toOutputStream(os);
        byte[] array = os.toByteArray();
        InputStream is = new ByteArrayInputStream(array);

        ObjectMetadata thumbMetaData = new ObjectMetadata();
        thumbMetaData.setContentType(arquivo.getContentType());
        thumbMetaData.setContentLength(array.length);
        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, THUMBNAIL_PREFIX + novoNome, is, thumbMetaData).withAccessControlList(acs));
    }
}
