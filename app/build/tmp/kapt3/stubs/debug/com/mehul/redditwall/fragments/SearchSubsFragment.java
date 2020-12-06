package com.mehul.redditwall.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 $2\u00020\u0001:\u0001$B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0019\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\tH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J&\u0010\u001a\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u0010!\u001a\u00020\u0012H\u0016J\u001a\u0010\"\u001a\u00020\u00122\u0006\u0010#\u001a\u00020\u00142\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006%"}, d2 = {"Lcom/mehul/redditwall/fragments/SearchSubsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/mehul/redditwall/databinding/FragmentSearchSubsBinding;", "binding", "getBinding", "()Lcom/mehul/redditwall/databinding/FragmentSearchSubsBinding;", "noResMessage", "", "subAdapter", "Lcom/mehul/redditwall/adapters/SubAdapter;", "subViewModel", "Lcom/mehul/redditwall/viewmodels/SubViewModel;", "subsList", "Ljava/util/ArrayList;", "Lcom/mehul/redditwall/objects/Subreddit;", "findSubreddits", "", "v", "Landroid/view/View;", "getCon", "Landroid/content/Context;", "getSearchResults", "query", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "Companion", "app_debug"})
public final class SearchSubsFragment extends androidx.fragment.app.Fragment {
    private com.mehul.redditwall.databinding.FragmentSearchSubsBinding _binding;
    private final java.lang.String noResMessage = "No subreddits found";
    private final java.util.ArrayList<com.mehul.redditwall.objects.Subreddit> subsList = null;
    private com.mehul.redditwall.adapters.SubAdapter subAdapter;
    private com.mehul.redditwall.viewmodels.SubViewModel subViewModel;
    public static final com.mehul.redditwall.fragments.SearchSubsFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final com.mehul.redditwall.databinding.FragmentSearchSubsBinding getBinding() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void findSubreddits(android.view.View v) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    private final android.content.Context getCon() {
        return null;
    }
    
    public SearchSubsFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.mehul.redditwall.fragments.SearchSubsFragment newInstance() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007\u00a8\u0006\u0005"}, d2 = {"Lcom/mehul/redditwall/fragments/SearchSubsFragment$Companion;", "", "()V", "newInstance", "Lcom/mehul/redditwall/fragments/SearchSubsFragment;", "app_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.mehul.redditwall.fragments.SearchSubsFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}