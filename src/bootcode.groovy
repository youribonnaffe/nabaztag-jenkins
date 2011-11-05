application.log "bootcode, params=" + params


def bootcode = new File("bootcode.bin")
def bootcodeAsBytes = bootcode.bytes
response.contentLength = bootcodeAsBytes.length

sout.write bootcodeAsBytes
//sendBootCodeSlowly(bootcode)
response.status = 200


def sendBootCodeSlowly(bootcode) {
    def fin = bootcode.newInputStream()
    while (true) {
        int available = fin.available();
        if (available > 0) {
            int bufferSize = available;
            int BUFFER_SIZE = 1000;
            if (available > BUFFER_SIZE)
                bufferSize = BUFFER_SIZE;
            byte[] buffer = new byte[bufferSize];
            fin.read(buffer);
            sout.write(buffer);
            sout.flush();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Ignoring this exception
            }
            continue;
        }
        break;
    }
}
