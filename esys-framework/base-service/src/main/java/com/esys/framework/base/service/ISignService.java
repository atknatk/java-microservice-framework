package com.esys.framework.base.service;

import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public interface ISignService  {

    byte[] sign(byte[] data, String keyFile) throws InvalidKeyException, Exception;

    byte[] sign(String data, String keyFile) throws InvalidKeyException, Exception;

    PrivateKey getPrivate(String filename) throws Exception;

    boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception;

    PublicKey getPublic(String filename) throws Exception;


}
