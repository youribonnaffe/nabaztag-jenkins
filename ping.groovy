import groovy.json.JsonSlurper

application.log "ping, params=" + params

color = jenkinsStatus()

final DOUBLE_CLICK = '1'
final byte REBOOT = 0x9
final byte PING_INTERVAL = 0x3
final EARS_COLORS = 0x4

def fullPacket = [0x7F]
def pingInterval = 10
def pingIntervalPacket = [PING_INTERVAL, 0x0, 0x0, 0x1, pingInterval]

fullPacket += pingIntervalPacket

def colorsPacket = [EARS_COLORS, 0x0, 0x0, 0x1C, 0x7F, 0xFF, 0xFF, 0xFF]
earPos = 0
if(color == 1)
    earPos = 10
if(color == 3)
    earPos = 7
colorsPacket += [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, earPos, earPos, 0, color, color, color, 0, 0]

fullPacket += colorsPacket

if (params.sd == DOUBLE_CLICK)
    fullPacket = [0x7F, REBOOT, 0x0, 0x0, 0x1, 0x7F, 0xFF, 0xFF, 0xFF, 0xFF, 0xA]

fullPacket += [0xFF, 0xA]

bytePacket = fullPacket as byte[]
application.log "packet" + bytePacket
response.contentLength = bytePacket.length
sout.write bytePacket
response.status = 200

def jenkinsStatus() {
    final jenkinsUrl = "http://ci.jenkins-ci.org/view/All%20Disabled/"
    def slurper = new JsonSlurper()
    def url = (jenkinsUrl + "api/json").toURL()
    def result = slurper.parseText(url.text)
    final colors = result.jobs.color
    application.log "jobs= " + colors.count {it == 'blue'} + " " + colors.count {it == 'yellow'} + " " + colors.count {it == 'red'}
    if (colors.grep {it == 'red'}) return 1
    if (colors.grep {it == 'yellow'}) return 3
    return 0
}