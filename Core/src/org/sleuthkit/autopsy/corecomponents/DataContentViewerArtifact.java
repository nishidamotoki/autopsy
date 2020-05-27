/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011-2020 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.corecomponents;

import java.awt.Component;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.SwingWorker;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardArtifact.ARTIFACT_TYPE;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.TskCoreException;
import org.sleuthkit.datamodel.TskException;
import java.util.Arrays;
import java.util.Collections;
import org.sleuthkit.autopsy.contentviewers.ArtifactContentViewer;
import org.sleuthkit.autopsy.contentviewers.CallLogArtifactViewer;
import org.sleuthkit.autopsy.contentviewers.ContactArtifactViewer;
import org.sleuthkit.autopsy.contentviewers.DefaultArtifactContentViewer;

/**
 * Instances of this class display the BlackboardArtifacts associated with the
 * Content represented by a Node. 
 * 
 * It goes through a list of known ArtifactContentViewer to find a viewer that 
 * supports a given artifact and then hands it the artifact to display.
 */
@ServiceProvider(service = DataContentViewer.class, position = 7)
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
public class DataContentViewerArtifact extends javax.swing.JPanel implements DataContentViewer {

    private static final long serialVersionUID = 1L;
     
    @NbBundle.Messages({
        "DataContentViewerArtifact.failedToGetSourcePath.message=Failed to get source file path from case database",
        "DataContentViewerArtifact.failedToGetAttributes.message=Failed to get some or all attributes from case database"
    })
    private final static Logger logger = Logger.getLogger(DataContentViewerArtifact.class.getName());
    private final static String WAIT_TEXT = NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.waitText");
    private final static String ERROR_TEXT = NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.errorText");
   
    private Node currentNode; // @@@ Remove this when the redundant setNode() calls problem is fixed. 
    private int currentPage = 1;
    private final Object lock = new Object();
    private List<BlackboardArtifact> artifactTableContents; // Accessed by multiple threads, use getArtifactContents() and setArtifactContents()
    private SwingWorker<ViewUpdate, Void> currentTask; // Accessed by multiple threads, use startNewTask()
    

    private final Collection<ArtifactContentViewer> KNOWN_ARTIFACT_VIEWERS = 
            Arrays.asList(
                    new ContactArtifactViewer(),
                    new CallLogArtifactViewer()
            );
    
    public DataContentViewerArtifact() {
       
        initComponents();
       
        resetComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        totalPageLabel = new javax.swing.JLabel();
        ofLabel = new javax.swing.JLabel();
        currentPageLabel = new javax.swing.JLabel();
        pageLabel = new javax.swing.JLabel();
        nextPageButton = new javax.swing.JButton();
        pageLabel2 = new javax.swing.JLabel();
        prevPageButton = new javax.swing.JButton();
        artifactLabel = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        artifactContentPanel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(100, 58));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel1.setPreferredSize(new java.awt.Dimension(620, 58));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        totalPageLabel.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.totalPageLabel.text")); // NOI18N
        totalPageLabel.setMaximumSize(new java.awt.Dimension(40, 16));
        totalPageLabel.setPreferredSize(new java.awt.Dimension(25, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 0, 0);
        jPanel1.add(totalPageLabel, gridBagConstraints);

        ofLabel.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.ofLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 0, 0);
        jPanel1.add(ofLabel, gridBagConstraints);

        currentPageLabel.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.currentPageLabel.text")); // NOI18N
        currentPageLabel.setMaximumSize(new java.awt.Dimension(38, 14));
        currentPageLabel.setMinimumSize(new java.awt.Dimension(18, 14));
        currentPageLabel.setPreferredSize(new java.awt.Dimension(20, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 0, 0);
        jPanel1.add(currentPageLabel, gridBagConstraints);

        pageLabel.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.pageLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 12, 0, 0);
        jPanel1.add(pageLabel, gridBagConstraints);

        nextPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_forward.png"))); // NOI18N
        nextPageButton.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.nextPageButton.text")); // NOI18N
        nextPageButton.setBorderPainted(false);
        nextPageButton.setContentAreaFilled(false);
        nextPageButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_forward_disabled.png"))); // NOI18N
        nextPageButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        nextPageButton.setPreferredSize(new java.awt.Dimension(23, 23));
        nextPageButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_forward_hover.png"))); // NOI18N
        nextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 35, 0);
        jPanel1.add(nextPageButton, gridBagConstraints);

        pageLabel2.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.pageLabel2.text")); // NOI18N
        pageLabel2.setMaximumSize(new java.awt.Dimension(29, 14));
        pageLabel2.setMinimumSize(new java.awt.Dimension(29, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 41, 0, 0);
        jPanel1.add(pageLabel2, gridBagConstraints);

        prevPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_back.png"))); // NOI18N
        prevPageButton.setText(org.openide.util.NbBundle.getMessage(DataContentViewerArtifact.class, "DataContentViewerArtifact.prevPageButton.text")); // NOI18N
        prevPageButton.setBorderPainted(false);
        prevPageButton.setContentAreaFilled(false);
        prevPageButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_back_disabled.png"))); // NOI18N
        prevPageButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        prevPageButton.setPreferredSize(new java.awt.Dimension(23, 23));
        prevPageButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_back_hover.png"))); // NOI18N
        prevPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevPageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 35, 0);
        jPanel1.add(prevPageButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 8);
        jPanel1.add(artifactLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel1.add(filler1, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel1);

        artifactContentPanel.setLayout(new javax.swing.OverlayLayout(artifactContentPanel));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
            .addComponent(artifactContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(artifactContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageButtonActionPerformed
        currentPage += 1;
        currentPageLabel.setText(Integer.toString(currentPage));
        artifactLabel.setText(artifactTableContents.get(currentPage - 1).getDisplayName());
        startNewTask(new SelectedArtifactChangedTask(currentPage));
    }//GEN-LAST:event_nextPageButtonActionPerformed

    private void prevPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevPageButtonActionPerformed
        currentPage -= 1;
        currentPageLabel.setText(Integer.toString(currentPage));
        artifactLabel.setText(artifactTableContents.get(currentPage - 1).getDisplayName());
        startNewTask(new SelectedArtifactChangedTask(currentPage));
    }//GEN-LAST:event_prevPageButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel artifactContentPanel;
    private javax.swing.JLabel artifactLabel;
    private javax.swing.JLabel currentPageLabel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nextPageButton;
    private javax.swing.JLabel ofLabel;
    private javax.swing.JLabel pageLabel;
    private javax.swing.JLabel pageLabel2;
    private javax.swing.JButton prevPageButton;
    private javax.swing.JLabel totalPageLabel;
    // End of variables declaration//GEN-END:variables


    /**
     * Resets the components to an empty view state.
     */
    private void resetComponents() {
        currentPage = 1;
        currentPageLabel.setText("");
        artifactLabel.setText("");
        totalPageLabel.setText("");
       
        prevPageButton.setEnabled(false);
        nextPageButton.setEnabled(false);
        currentNode = null;
        
        artifactContentPanel.removeAll();
    }

    @Override
    public void setNode(Node selectedNode) {
        if (currentNode == selectedNode) {
            return;
        }
        currentNode = selectedNode;

        // Make sure there is a node. Null might be passed to reset the viewer.
        if (selectedNode == null) {
            return;
        }

        // Make sure the node is of the correct type.
        Lookup lookup = selectedNode.getLookup();
        Content content = lookup.lookup(Content.class);
        if (content == null) {
            return;
        }

        startNewTask(new SelectedNodeChangedTask(selectedNode));
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(this.getClass(), "DataContentViewerArtifact.title");
    }

    @Override
    public String getToolTip() {
        return NbBundle.getMessage(this.getClass(), "DataContentViewerArtifact.toolTip");
    }

    @Override
    public DataContentViewer createInstance() {
        return new DataContentViewerArtifact();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {
        resetComponents();
    }

    @Override
    public boolean isSupported(Node node) {
        if (node == null) {
            return false;
        }

        for (Content content : node.getLookup().lookupAll(Content.class)) {
            if ( (content != null)  && (!(content instanceof BlackboardArtifact)) ){
                try {
                    return content.getAllArtifactsCount() > 0;
                } catch (TskException ex) {
                    logger.log(Level.SEVERE, "Couldn't get count of BlackboardArtifacts for content", ex); //NON-NLS
                }
            }
        }
        return false;
    }

    @Override
    public int isPreferred(Node node) {
        BlackboardArtifact artifact = node.getLookup().lookup(BlackboardArtifact.class);
        // low priority if node doesn't have an artifact (meaning it was found from normal directory
        // browsing, or if the artifact is something that means the user really wants to see the original
        // file and not more details about the artifact
        if ((artifact == null)
                || (artifact.getArtifactTypeID() == ARTIFACT_TYPE.TSK_HASHSET_HIT.getTypeID())
                || (artifact.getArtifactTypeID() == ARTIFACT_TYPE.TSK_KEYWORD_HIT.getTypeID())
                || (artifact.getArtifactTypeID() == ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT.getTypeID())
                || (artifact.getArtifactTypeID() == ARTIFACT_TYPE.TSK_OBJECT_DETECTED.getTypeID())
                || (artifact.getArtifactTypeID() == ARTIFACT_TYPE.TSK_METADATA_EXIF.getTypeID())
                || (artifact.getArtifactTypeID() == ARTIFACT_TYPE.TSK_EXT_MISMATCH_DETECTED.getTypeID())) {
            return 3;
        } else {
            return 6;
        }
    }

    private ArtifactContentViewer getSupportingViewer(BlackboardArtifact artifact) {
        
        return this.KNOWN_ARTIFACT_VIEWERS.stream()
                .filter(knownViewer -> knownViewer.isSupported(artifact))
                .findAny()
                .orElse(new DefaultArtifactContentViewer());
                
    }

    /**
     * Instances of this class are simple containers for view update information
     * generated by a background thread.
     */
    private class ViewUpdate {

        int numberOfPages;
        int currentPage;
        BlackboardArtifact artifact;
        String errorMsg;

        ViewUpdate(int numberOfPages, int currentPage, BlackboardArtifact artifact) {
            this.currentPage = currentPage;
            this.numberOfPages = numberOfPages;
            this.artifact = artifact;
            this.errorMsg = null;
        }

        ViewUpdate(int numberOfPages, int currentPage, String errorMsg) {
            this.currentPage = currentPage;
            this.numberOfPages = numberOfPages;
            this.errorMsg = errorMsg;
            this.artifact = null;
        }
    }

    /**
     * Called from queued SwingWorker done() methods on the EDT thread, so
     * doesn't need to be synchronized.
     *
     * @param viewUpdate A simple container for display update information from
     *                   a background thread.
     */
    private void updateView(ViewUpdate viewUpdate) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        nextPageButton.setEnabled(viewUpdate.currentPage < viewUpdate.numberOfPages);
        prevPageButton.setEnabled(viewUpdate.currentPage > 1);
        currentPage = viewUpdate.currentPage;
        totalPageLabel.setText(Integer.toString(viewUpdate.numberOfPages));
        currentPageLabel.setText(Integer.toString(currentPage));
        
        
        artifactContentPanel.removeAll();
        
        if (viewUpdate.artifact != null) {
            artifactLabel.setText(viewUpdate.artifact.getDisplayName());

            BlackboardArtifact artifact = viewUpdate.artifact;
            ArtifactContentViewer viewer = this.getSupportingViewer(artifact);
            viewer.setArtifact(artifact);

            artifactContentPanel.add(viewer.getComponent());
        } else {
            artifactLabel.setText(viewUpdate.errorMsg);
        }
        
        artifactContentPanel.revalidate();
        this.setCursor(null);
        
        this.revalidate();
    }

    /**
     * Start a new task on its own background thread, canceling the previous
     * task.
     *
     * @param task A new SwingWorker object to execute as a background thread.
     */
    private synchronized void startNewTask(SwingWorker<ViewUpdate, Void> task) {
        
        // The output of the previous task is no longer relevant.
        if (currentTask != null) {
            // This call sets a cancellation flag. It does not terminate the background thread running the task. 
            // The task must check the cancellation flag and react appropriately.
            currentTask.cancel(false);
        }

        // Start the new task.
        currentTask = task;
        currentTask.execute();
    }

    /**
     * Populate the cache of artifacts represented as ResultsTableArtifacts.
     *
     * @param artifactList A list of ResultsTableArtifact representations of
     *                     artifacts.
     */
    private void setArtifactContents(List<BlackboardArtifact> artifactList) {
        synchronized (lock) {
            this.artifactTableContents = artifactList;
        }
    }

    /**
     * Retrieve the cache of artifact represented as ResultsTableArtifacts.
     *
     * @return A list of artifacts.
     */
    private List<BlackboardArtifact> getArtifactContents() {
        synchronized (lock) {
            return Collections.unmodifiableList(artifactTableContents);
        }
    }

    /**
     * Instances of this class use a background thread to generate a ViewUpdate
     * when a node is selected, changing the set of blackboard artifacts
     * ("results") to be displayed.
     */
    private class SelectedNodeChangedTask extends SwingWorker<ViewUpdate, Void> {

        private final Node selectedNode;

        SelectedNodeChangedTask(Node selectedNode) {
            this.selectedNode = selectedNode;
        }

        @Override
        protected ViewUpdate doInBackground() {
            // Get the lookup for the node for access to its underlying content and
            // blackboard artifact, if any.
            Lookup lookup = selectedNode.getLookup();

            // Get the content. We may get BlackboardArtifacts, ignore those here.
            ArrayList<BlackboardArtifact> artifacts = new ArrayList<>();
            Collection<? extends Content> contents = lookup.lookupAll(Content.class);
            if (contents.isEmpty()) {
                return new ViewUpdate(getArtifactContents().size(), currentPage, ERROR_TEXT);
            }
            Content underlyingContent = null;
            for (Content content : contents) {
                if ( (content != null)  && (!(content instanceof BlackboardArtifact)) ) {
                    // Get all of the blackboard artifacts associated with the content. These are what this
                    // viewer displays.
                    try {
                        artifacts = content.getAllArtifacts();
                        underlyingContent = content;
                        break;
                    } catch (TskException ex) {
                        logger.log(Level.SEVERE, "Couldn't get artifacts", ex); //NON-NLS
                        return new ViewUpdate(getArtifactContents().size(), currentPage, ERROR_TEXT);
                    }
                }
            }
 
            if (isCancelled()) {
                return null;
            }

            // Build the new artifact contents cache.
            ArrayList<BlackboardArtifact> artifactContents = new ArrayList<>();
            for (BlackboardArtifact artifact : artifacts) {
                artifactContents.add(artifact);
            }

            // If the node has an underlying blackboard artifact, show it. If not,
            // show the first artifact.
            int index = 0;
            BlackboardArtifact artifact = lookup.lookup(BlackboardArtifact.class);
            if (artifact != null) {
                index = artifacts.indexOf(artifact);
                if (index == -1) {
                    index = 0;
                } else {
                    // if the artifact has an ASSOCIATED ARTIFACT, then we display the associated artifact instead
                    try {
                        for (BlackboardAttribute attr : artifact.getAttributes()) {
                            if (attr.getAttributeType().getTypeID() == BlackboardAttribute.ATTRIBUTE_TYPE.TSK_ASSOCIATED_ARTIFACT.getTypeID()) {
                                long assocArtifactId = attr.getValueLong();
                                int assocArtifactIndex = -1;
                                for (BlackboardArtifact art : artifacts) {
                                    if (assocArtifactId == art.getArtifactID()) {
                                        assocArtifactIndex = artifacts.indexOf(art);
                                        break;
                                    }
                                }
                                if (assocArtifactIndex >= 0) {
                                    index = assocArtifactIndex;
                                }
                                break;
                            }
                        }
                    } catch (TskCoreException ex) {
                        logger.log(Level.WARNING, "Couldn't get associated artifact to display in Content Viewer.", ex); //NON-NLS
                    }
                }

            }

            if (isCancelled()) {
                return null;
            }

            // Add one to the index of the artifact content for the corresponding page index.
            ViewUpdate viewUpdate = new ViewUpdate(artifactContents.size(), index + 1, artifactContents.get(index));

            // It may take a considerable amount of time to fetch the attributes of the selected artifact 
            if (isCancelled()) {
                return null;
            }

            // Update the artifact contents cache.
            setArtifactContents(artifactContents);

            return viewUpdate;
        }

        @Override
        protected void done() {
            if (!isCancelled()) {
                try {
                    ViewUpdate viewUpdate = get();
                    if (viewUpdate != null) {
                        updateView(viewUpdate);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    logger.log(Level.WARNING, "Artifact display task unexpectedly interrupted or failed", ex);                 //NON-NLS
                }
            }
        }
    }

    /**
     * Instances of this class use a background thread to generate a ViewUpdate
     * when the user pages the view to look at another blackboard artifact
     * ("result").
     */
    private class SelectedArtifactChangedTask extends SwingWorker<ViewUpdate, Void> {

        private final int pageIndex;

        SelectedArtifactChangedTask(final int pageIndex) {
            this.pageIndex = pageIndex;
        }

        @Override
        protected ViewUpdate doInBackground() {
            // Get the artifact content to display from the cache. Note that one must be subtracted from the
            // page index to get the corresponding artifact content index.
            List<BlackboardArtifact> artifactContents = getArtifactContents();
          
            // It may take a considerable amount of time to fetch the attributes of the selected artifact so check for cancellation.
            if (isCancelled()) {
                return null;
            }

            BlackboardArtifact artifactContent = artifactContents.get(pageIndex - 1);
            return new ViewUpdate(artifactContents.size(), pageIndex, artifactContent);
        }

        @Override
        protected void done() {
            if (!isCancelled()) {
                try {
                    ViewUpdate viewUpdate = get();
                    if (viewUpdate != null) {
                        updateView(viewUpdate);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    logger.log(Level.WARNING, "Artifact display task unexpectedly interrupted or failed", ex);                 //NON-NLS
                }
            }
        }
    }
}
