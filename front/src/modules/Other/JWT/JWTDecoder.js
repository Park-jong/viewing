function atobSafe(base64) {
    return atob(base64.replace(/-/g, '+').replace(/_/g, '/'));
}

export function JWTDecoder(jwtToken) {
    try {
        const [headerB64, payloadB64, signatureB64] = jwtToken.split('.');

        const header = JSON.parse(atobSafe(headerB64));
        const payload = JSON.parse(atobSafe(payloadB64));
        const signature = atobSafe(signatureB64);

        return {
            header,
            payload,
            signature
        };
    } catch (error) {
        return null;
    }
}