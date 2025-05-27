package com.example.manga_ln_app.presentation.post_content

import android.net.Uri
import com.example.manga_ln_app.domain.model.ListItem
import com.example.manga_ln_app.domain.model.Type

sealed interface PostPageAction {
    data class OnSelectedDropdownOneChange( val type: Type ): PostPageAction
    data class OnSelectedDropdownTwoChange( val contentItem: ListItem.Content ): PostPageAction
    data class OnContentNameChange( val contentName: String ): PostPageAction
    data class OnChapterNameChange( val chapterName: String ): PostPageAction
    data object OnPostButtonOneClick: PostPageAction
    data object OnPostButtonTwoClick: PostPageAction
    data class OnChangeSelectedUris( val selectedUris: List<Uri> ): PostPageAction
}