export class AnalysisResponse  {

    constructor(errors : Array<String>) {
        this.errors   = errors;
    }
    errors: Array<String>

    emptyObject(){
        let retval = new AnalysisResponse([]);
        return retval;
    }
}