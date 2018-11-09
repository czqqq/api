package com.api.controller.util;

public class AliPayUtil {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018102561858016";

    /** 支付宝网关*/
    public static final String GATE = "https://openapi.alipay.com/gateway.do";

    /** 支付宝私钥*/
    public static final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDTeakEJ4/jweWJuAd04BW2c+0gslU4Ni8gnRSnQFUz1Zgn13/7AXmn/OlRM6VUZy3PDKdmJcLHhi5R1zop5GH2+ASjEGbT7xFkBQonwuM+3tPuq955d6mE04zWKGBGDCZ2DjGZ7/RDeaw+vK6jsJtSDxhEKBHRblWJoU9vGhc/Lg+UtJlaBMfd5k2XoZjz7tIQh2UfLA7EdZyhCKw2BPDHoKHxwh0jxq2EJ1IBvk3s/bNq/P7bQICabuSuhKd8jJRuJBuXdL2nipu+X/zkQARGbe8ehvvl4cFYHDV5cTeL8Upud0Jcy65BQVB9rD5EELprVi/JL02J03hArEhkUx//AgMBAAECggEBANCTB64/Tt22GaxxQ3l0ojWntNMw0Hh2iJjEFOC4xNAw9LcnBuUMGW8kNQKPy5qpgTbbJifLSob7wqeZfZZf2Hd7OyeRP7n76s0ZY2DACN2zbJfUD8MB0MJe3GNEw1+TEEQTz7wiPGpokQf3lM+Bmk+e77IZSymiht8NqIjN1OvLuRPgMtUMMbDYF46FO10JGAenwelzSt4iSl/prZekLj3dO6ScboZn+Ic5rPVaweyT3DByti2zo+tvQ9Srp85ya/OMGv7YrF6B7gbwOkW8KB6PFiMBcqBR9zQcVyXmX1Qs+BOsmOd8kTroEGNB8YBjJ75fHJV2ePf2Tdab2MYy4/ECgYEA7utdjuuluD0eaey0sNtO1h50p46eUrabTZFbO9sgRhz1+FADD1RXg3Dv9zHUzjBQDu6XON6cau4pHHpL+4wYwMRrhDz058jITHIzXwgNDP0cjGyejxHxODHGYtJPMJDiiwdonxiFytWQq/BaF/q8WX+NIzdcxcooQ7sQkUp4ZuUCgYEA4pgFAfdu0iwrDRutokNlj/J8ZqJpK3lD7xC9G9EHSjxcQRUp10NA8P/PEtRhdUCQfXUvkz46VRepTASqXkn3mX+5Nv+NI1E/nlKCljdsiWERfd8lh4neHQDm+IvQrmfN6bjc7Exe0imty0PL6KK9WweXvKLbOdHfyzE4AfZouRMCgYAcsbnf019o3DZN5HbjEsPKJYg/I3rSVkoaq814R939bbcA5qMncSq9h4/acWqshNLoqDETTHEeypG3cNy1TmMeq7h5/lQvjka4H7MZw77Fg5StLNJyPmZpKFk6PyXxoEUFC5O+H9TZwoKIk+pOHPgIC7CovRpwStJBKDNaL8Z8HQKBgGVV8R9hBcmugQ5aq3tewsPugN3xkjEkCVKX3Mrc33K+rc9ZZJ3lcNyWuofEPVWPq07AIqep87JsY4UgrQR/9eBK84z6GqeJxXbLlaUSYlLJ+2Wcbw8/g/ralXDgLlJhEACPcRWoTduVUikOR9nhNefI7H4rIvmnt3sKpu6RwWHLAoGBAOPEOn9U6UUOQ+EcHz2e+/sU58HUOS4h4DsnbpEdWaAjmlNtZMyfMj326mJjmotqcQtA++iaFpQkqXzbeHKtDJlBjYPawLn9i2PNBKRbeMPUMdvFaqePt3JvEK7EoVgmptC0ChM47BJizIKMJjo8Pwa+eHgHENPk0WAuVNRQuoQ+";

    /** 支付宝公钥 */
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnkhBMNaG7i1OVntyejU83Fzwvgpwp59pIcYciBU/WKNlU2++ChIe6RigdP5/Ucl9dkBA2qLWXj24zb8mTDKwtQor1AXF73y6CiRk7DFp1deCUnbML6ZerXL/nb2WeCiOb2xBJVQEMGT4JNLtOea1MqccGhxCs2/knPpyYEQbBVKy4UMktB0FY0ZoeBQo2urRSEQM+ynA+MMSupi79j3djbA8X8OHDh1YToMAr6cO/aoKmH89ka2XpOHCU/+huTXBx41Dsd5aXjjOr78QreL00gdhR6dvVR8wTWJXs9CioBw8smq29srzVi5S+/YjF13ffDuL4y0c6UJzAVuey+MNzwIDAQAB";

    /** 编码方式 */
    public static final String CHARSET = "utf-8";

    public static final String NOTIFY_URL="http://47.107.79.61/api/notify_url";
    public static final String RETURN_URL = "http://47.107.79.61/api/return_url";
}
