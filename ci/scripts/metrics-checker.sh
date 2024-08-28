#!/bin/sh
#
# COPYRIGHT Ericsson 2021
#
#
#
# The copyright to the computer program(s) herein is the property of
#
# Ericsson Inc. The programs may be used and/or copied only with written
#
# permission from Ericsson Inc. or in accordance with the terms and
#
# conditions stipulated in the agreement/contract under which the
#
# program(s) have been supplied.
#

METRICS_EXPOSURE_TUTORIAL_URL="https://confluence-oss.seli.wh.rnd.internal.ericsson.com/pages/viewpage.action?spaceKey=ESO&title=How+to+add+metrics+to+a+microservice";

checkValuesYAML(){
    echo "Skipped, not using spring boot"
}

checkServiceYAML(){
    echo "Skipped, not using spring boot"
}

checkDeploymentYAML(){
    echo "Skipped, not using spring boot"
}

checkConfigMapYAML(){
    echo "Skipped, not using spring boot"
}

checkHelperTPL(){
    echo "Skipped, not using spring boot"
}

checkPomXML(){
    echo "Skipped, not using spring boot"
}

checkCoreApplicationJAVA(){
    echo "Skipped, not using spring boot"
}

passOrFailCheck(){
    if [ ! -s .bob/var.metrics-exposed ]; then
        echo "SUCCESS: All necessary lines for metrics exposure implemented correctly.";
    else
        for check in {"HelperTPL","CoreApplicationJAVA","PomXML","ValuesYAML","ConfigMapYAML"}
        do
            if grep -q "$check" .bob/var.metrics-exposed; then
               echo "FAILURE: Please review console output to find the files which should be corrected.";
               exit 1;
            fi
        done
        if grep -q "ServiceYAML" .bob/var.metrics-exposed && grep -q "DeploymentYAML" .bob/var.metrics-exposed; then
            echo "FAILURE: Please review console output to find the files which should be corrected.";
            exit 1;
        else
            echo "SUCCESS: All necessary lines for metrics exposure implemented correctly.";
        fi
    fi

}
