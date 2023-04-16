package CH.ifa.draw.samples.javadraw;

import CH.ifa.draw.framework.DrawingEditor;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.samples.pert.PertFigure;
import CH.ifa.draw.standard.CreationTool;

public class EntityRelationshipTool extends CreationTool {

    public EntityRelationshipTool(DrawingEditor newDrawingEditor) {
        super(newDrawingEditor);
    }

    /**
     * Creates a new PertFigure.
     */
    protected Figure createFigure() {
        return new EntityRelationshipFigure();
    }
}
