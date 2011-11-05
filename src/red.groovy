application.log "red"

def chor = new Choreography()
def t = 1
for (int i = 0; i < 30; i += 2) {
    chor.addTempoCommand(i, t);
    chor.addLedColorCommand(i, Choreography.LED_CENTER, 255, 0, 0);
    chor.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 0, 0);
    chor.addLedColorCommand(0, Choreography.LED_NOSE, 0, 255, 0);

    chor.addTempoCommand(i + 1, t);
    chor.addLedColorCommand(i + 1, Choreography.LED_CENTER, 0, 0, 0);
    chor.addLedColorCommand(0, Choreography.LED_BOTTOM, 0, 0, 0);

}


response.contentLength = chor.data.length
sout.write chor.data.length
sout.write chor.data
response.status = 200