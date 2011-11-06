application.log "bootcode, params=" + params

def bootcode = new File("bootcode-jenkins.bin")
def bootcodeAsBytes = bootcode.bytes
response.contentLength = bootcodeAsBytes.length

sout.write bootcodeAsBytes
response.status = 200
