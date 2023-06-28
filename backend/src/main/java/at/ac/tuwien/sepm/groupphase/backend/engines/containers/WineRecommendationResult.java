package at.ac.tuwien.sepm.groupphase.backend.engines.containers;

import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;

/**
 * This container contains the results of a wine recommendation.
 */
public class WineRecommendationResult {
    private WineCategory category;

    private double confidence;

    public WineRecommendationResult(WineCategory category, double confidence) {
        this.category = category;
        this.confidence = confidence;
    }

    public WineCategory getCategory() {
        return category;
    }

    public void setCategory(WineCategory category) {
        this.category = category;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public static final class WineRecommendationResultBuilder {
        private WineCategory category;
        private double confidence;

        public WineRecommendationResultBuilder category(WineCategory category) {
            this.category = category;
            return this;
        }

        public WineRecommendationResultBuilder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }

        public WineRecommendationResult build() {
            return new WineRecommendationResult(category, confidence);
        }
    }

}
