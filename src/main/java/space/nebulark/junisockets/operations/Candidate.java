package space.nebulark.junisockets.operations;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class Candidate implements ICandidate {
    public ESignalingOperationCode opcode = ESignalingOperationCode.CANDIDATE;

    private String offererId;
    private String answererId;
    private String candidate;

    public Candidate(String offererId, String answererId, String candidate) {
        this.offererId = offererId;
        this.answererId = answererId;
        this.candidate = candidate;
    }

    public String getOffererId() {
        return offererId;
    }

    public String getAnswererId() {
        return answererId;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getAsJSON(Object operationObject) {

        Candidate operation = (Candidate) operationObject;

        JSONObject obj = new JSONObject();
        String jsonText;

        Map<Object, Object> m1 = new LinkedHashMap<Object, Object>();
        m1.put("offererId", (String) operation.getOffererId());
        m1.put("answererId", operation.getAnswererId());
        m1.put("candidate", operation.getCandidate());

        obj.put("data", m1);
        obj.put("opcode", operation.opcode.getValue());

        jsonText = obj.toString();
        ;

        return jsonText;
    }

    public String getOpCode() {
        return opcode.toString();
    }
}