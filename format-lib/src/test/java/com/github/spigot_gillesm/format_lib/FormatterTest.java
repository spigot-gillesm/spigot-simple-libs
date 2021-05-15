package com.github.spigot_gillesm.format_lib;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FormatterTest {

	@Test
	void given_null_message_to_colorize_then_empty_string_is_returned() {
		assertThat(Formatter.colorize((String) null)).isEmpty();
	}

	@Test
	void given_empty_message_to_colorize_then_empty_string_is_returned() {
		assertThat(Formatter.colorize("")).isEmpty();
	}

	@Test
	void given_non_empty_message_to_colorize_then_message_is_returned() {
		assertThat(Formatter.colorize("message")).isEqualTo("message");
	}

	@Test
	void given_null_to_colorize_multiple_then_illegal_argument_exception_is_thrown() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> Formatter.colorize((List<String>) null));
	}

	@Test
	void given_empty_list_to_colorize_then_empty_list_is_returned() {
		assertThat(Formatter.colorize(new ArrayList<>())).isEqualTo(Collections.emptyList());
	}

	@Test
	void given_non_empty_list_to_colorize_then_list_is_returned() {
		assertThat(Formatter.colorize(new ArrayList<>(Collections.singletonList("message"))))
				.isEqualTo(Collections.singletonList("message"));
	}

	@Test
	void given_null_command_sender_to_tell_then_illegal_argument_exception_is_thrown() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> Formatter.tell(null, "message"));
	}

	@Test
	void given_null_message_to_to_tell_then_nothing_happens() {
		final CommandSender sender = mock(CommandSender.class);
		Formatter.tell(sender, (String) null);
	}

	@Test
	void given_empty_message_to_tell_then_message_is_sent() {
		final CommandSender sender = mock(CommandSender.class);
		Formatter.tell(sender, "");
	}

	@Test
	void given_non_empty_message_to_tell_then_message_is_sent() {
		final CommandSender sender = mock(CommandSender.class);
		Formatter.tell(sender, "message");
	}

	@Test
	void given_null_message_to_tell_multiple_then_illegal_argument_exception_is_thrown() {
		final CommandSender sender = mock(CommandSender.class);
		Assertions.assertThrows(IllegalArgumentException.class, () -> Formatter.tell(sender, (String[]) null));
	}

	@Test
	void given_empty_list_to_tell_multiple_then_messages_are_sent() {
		final CommandSender sender = mock(CommandSender.class);
		Formatter.tell(sender, "");
	}

	@Test
	void given_non_empty_list_to_tell_multiple_then_messages_are_sent() {
		final CommandSender sender = mock(CommandSender.class);
		Formatter.tell(sender, "message", "message");
	}

	@Test
	void given_null_message_to_info_then_nothing_happens() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.info(null);
		mockBukkit.close();
	}

	@Test
	void given_empty_message_to_info_then_message_is_sent() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.info("");
		mockBukkit.close();
	}

	@Test
	void given_non_empty_message_to_info_then_message_is_sent() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.info("message");
		mockBukkit.close();
	}

	@Test
	void given_null_message_to_warning_then_nothing_happens() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.warning(null);
		mockBukkit.close();
	}

	@Test
	void given_empty_message_to_warning_then_message_is_sent() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.warning("");
		mockBukkit.close();
	}

	@Test
	void given_non_empty_message_to_warning_then_message_is_sent() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.warning("message");
		mockBukkit.close();
	}

	@Test
	void given_null_message_to_error_then_nothing_happens() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.error(null);
		mockBukkit.close();
	}

	@Test
	void given_empty_message_to_error_then_message_is_sent() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.error("");
		mockBukkit.close();
	}

	@Test
	void given_non_empty_message_to_error_then_message_is_sent() {
		final ConsoleCommandSender mockConsole = mock(ConsoleCommandSender.class);
		MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		mockBukkit.when(Bukkit::getConsoleSender).thenReturn(mockConsole);

		Formatter.error("message");
		mockBukkit.close();
	}

}