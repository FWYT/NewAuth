package com.example.helloworld.JWT;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

/**
 * Created by frances on 6/26/16.
 */
public class JwtClaim {

    public static RsaJsonWebKey generateKey()
    {
        try {
            //generate RSA key pair for signing and verifying jwt
            RsaJsonWebKey rsaKey = RsaJwkGenerator.generateJwk(2048);
            rsaKey.setKeyId("key");

            return rsaKey;
        } catch (Exception e)
        {
            System.out.println("\n");
            System.out.println("Unable to produce key");
            System.out.println("\n");
        }
        return null;
    }

    public static String createJwt(RsaJsonWebKey rsaKey)
    {
        try {
            JwtClaims claim = new JwtClaims();
            claim.setIssuer("Issuer");
            claim.setAudience("Audience");
            claim.setExpirationTimeMinutesInTheFuture(10);
            claim.setGeneratedJwtId();
            claim.setIssuedAtToNow();
            claim.setSubject("yay");

            //create a json web signature
            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claim.toJson());
            //sign using private key
            jws.setKey(rsaKey.getPrivateKey());
            jws.setKeyIdHeaderValue(rsaKey.getKeyId());
            //set signature algo
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

            //sign jwt and produce jwt representation
            return jws.getCompactSerialization();
        } catch (Exception e)
        {
            System.out.println("\n");
            System.out.println("Unable to produce JWT claim");
            System.out.println("\n");
        }

        return null;
    }

    public static JwtConsumer getJwtConsumer(RsaJsonWebKey key)
    {
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer("Issuer")
                .setExpectedAudience("Audience")
                .setRelaxVerificationKeyValidation()
                .setVerificationKey(key.getKey())
                .build();
        return consumer;
    }

    public static boolean validateJwt(RsaJsonWebKey key, String jwt)
    {
        JwtConsumer consumer = getJwtConsumer(key);

        try
        {
            JwtClaims claim = consumer.processToClaims(jwt);
            System.out.println("\n");
            System.out.println("VALIDATED");
            System.out.println("\n");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("\n");
            System.out.println("INVALID");
            System.out.println("\n");
        }
        return false;
    }
}
