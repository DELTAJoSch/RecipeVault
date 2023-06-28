/**
 * Represents an OcrTask
 */
export class OcrTask {
    id: number;
    status: boolean;
    step: OcrStep;
}

/**
 * The different Steps the OcrTask can be in
 */
export enum OcrStep {
    pending = 'PENDING',
    preparation = 'PREPARATION',
    reading = 'READING',
    finished = 'FINISHED'
}
