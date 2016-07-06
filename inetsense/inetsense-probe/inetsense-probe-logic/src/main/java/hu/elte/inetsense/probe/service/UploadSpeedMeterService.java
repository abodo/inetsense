package hu.elte.inetsense.probe.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hu.elte.inetsense.probe.service.configuration.ConfigurationNames;
import hu.elte.inetsense.probe.service.configuration.ConfigurationProvider;

public class UploadSpeedMeterService implements SpeedMeterService {

    private static final Logger log = LogManager.getLogger();
    private ConfigurationProvider configurationProvider;

    public UploadSpeedMeterService(ConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    @Override
    public long measure() throws Exception {
        return upload();
    }

    private long upload() throws Exception {
        log.info("Measuring upload speed...");
        URL u = getUploadUrl();
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(createBinaryData());
        os.flush();
        try(Scanner sc = new Scanner(conn.getInputStream())) {
            if(sc.hasNextLong()) {
                long nextLong = sc.nextLong();
                log.info("Upload done. Measured speed: {} Bps.", nextLong);
                return nextLong;
            }
        }
        return Long.MAX_VALUE;
    }

    private URL getUploadUrl() throws MalformedURLException {
        String host = configurationProvider.getString(ConfigurationNames.UPLOAD_SERVER_HOST);
//        host = "80.99.186.6";
        int port = configurationProvider.getInt(ConfigurationNames.UPLOAD_SERVER_PORT);
        return new URL(String.format("http://%s:%d/upload", host, port));
    }

    private byte[] createBinaryData() {
        int fileSize = configurationProvider.getInt(ConfigurationNames.PROBE_UPLOAD_SIZE);
        log.info("...Using upload data size: {} bytes.", fileSize);
        byte[] b = new byte[fileSize];
        Arrays.fill(b, (byte) 1);
        b[b.length-1] = '\n';
        return b;
    }

}
