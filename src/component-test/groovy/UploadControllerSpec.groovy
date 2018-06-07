import com.apijustgifit.service.FileStorageService
import spock.lang.Specification

class UploadControllerSpec extends Specification {
    def setupSpec() {
        reportHeader "<h2>Browser: </h2>"
    }
    def "one plus one should equal two"() {
        expect:
        reportInfo "Some information I want to show in the report"
        1 + 1 == 2
    }
}
