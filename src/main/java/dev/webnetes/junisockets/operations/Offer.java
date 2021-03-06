package dev.webnetes.junisockets.operations;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Offer
 */
public class Offer implements IOffer {
    private ESignalingOperationCode opcode = ESignalingOperationCode.OFFER;
    private String offererId;
    private String answererId;
    private String offer;

    /**
     * Constructor Offer
     * @param offererId offererId
     * @param answererId answererId
     * @param offer offer
     */
    public Offer(String offererId, String answererId, String offer) {
        this.offererId = offererId;
        this.answererId = answererId;
        this.offer = offer;
    }

    
    /**
     * Returns offererId 
     * @return String
     */
    public String getOffererId() {
        return offererId;
    }

    
    /**
     * Returns answererId 
     * @return String
     */
    public String getAnswererId() {
        return answererId;
    }

    
    /**
     * Returns offer 
     * @return String
     */
    public String getOffer() {
        return offer;
    }

    
    /** 
     * Returns operation as JSON. Warnings are suppressed because there are unavoidable ones when using json-simple in this case.
     * @param operationObject operation
     * @return String
     */
    @SuppressWarnings("unchecked")
    public String getAsJSON(Object operationObject) {

        Offer operation = (Offer) operationObject;

        JSONObject obj = new JSONObject();
        String jsonText;

        Map<Object, Object> m1 = new LinkedHashMap<Object, Object>();
        m1.put("offererId", (String) operation.getOffererId());
        m1.put("answererId", operation.getAnswererId());
        m1.put("offer", operation.getOffer());

        obj.put("data", m1);
        obj.put("opcode", operation.opcode.getValue());

        jsonText = obj.toString();

        return jsonText;
    }

    
    /** 
     * Returns opcode
     * @return String
     */
    public String getOpCode() {
        return opcode.toString();
    }
}
