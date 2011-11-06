import groovy.json.JsonSlurper

application.log "ping, params=" + params

color = jenkinsStatus()
final earsCode = [(Rabbit.Colors.RED): 10, (Rabbit.Colors.YELLOW): 7, (Rabbit.Colors.NONE): 0, (Rabbit.Colors.GREEN): 0]

rabbit = new Rabbit(params: params)
rabbit.with {
    interval = 10
    ears.left = earsCode.get color
    ears.right = earsCode.get color
    leds = color
    if (doubleClicked())
        reboot()
    send response
}

application.log "rabbit" + rabbit

def jenkinsStatus() {
    final jenkinsUrl = "http://ci.jenkins-ci.org/view/All%20Unstable/"
    def slurper = new JsonSlurper()
    def url = (jenkinsUrl + "api/json").toURL()
    def result = slurper.parseText(url.text)
    final colors = result.jobs.color
    application.log "jobs= " + colors.count {it == 'blue'} + " " + colors.count {it == 'yellow'} + " " + colors.count {it == 'red'}
    if (colors.grep {it == 'red'}) return Rabbit.Colors.RED
    if (colors.grep {it == 'yellow'}) return Rabbit.Colors.YELLOW
    return Rabbit.Colors.NONE
}
